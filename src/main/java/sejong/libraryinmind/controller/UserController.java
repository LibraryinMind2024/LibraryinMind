package sejong.libraryinmind.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.service.UserService;
import sejong.libraryinmind.util.JwtTokenProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
//CustomerRepository 속성을 포함하는 생성자를 자동으로 생성하는 것
@Controller
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @RequestMapping("/customer")
    //url과 customer 매핑
    public String list(Model model){

        List<UserEntity> userEntityList = this.userService.getList();
        model.addAttribute("customerEntityList", userEntityList);

        return  "Customer";
    }

    @RequestMapping("/")
    public String root(Model model){
        model.addAttribute("username", userService.getLoggedInUsername());
        model.addAttribute("name", userService.getLoggedInName());

        return "main";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("username", userService.getLoggedInUsername());
        model.addAttribute("name", userService.getLoggedInName());

        return "main";
    }

    //회원가입 페이지 이동
    @RequestMapping("/signup")
    public String showSignup(Model model){

        List<UserEntity> userEntityList = this.userService.getList();
        model.addAttribute("customerEntityList", userEntityList);

        return  "signup";
    }

    //회원가입 처리
    @PostMapping("/signup/create")
    public String Signup(@RequestParam String name, String username, String password){
        this.userService.create(name,username,password);

        return "redirect:/signup";
    }


    // 로그인 페이지 이동
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }


    // 로그인 처리

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        Optional<UserEntity> userOptional = userService.validateUser(username, password);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // JWT 생성
            String token = jwtTokenProvider.createToken(user.getUsername());


            // 세션에 사용자 정보 저장
            session.setAttribute("user", user);

            // 응답에 토큰 포함
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 성공");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } else {
            // 실패 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "아이디 또는 패스워드가 잘못 입력되었습니다."
            ));
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션에서 사용자 이름 제거
        session.invalidate();  // 세션을 무효화하여 모든 속성 제거

        // 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String showMypage(Model model) {
        UserEntity user = userService.getLoggedInUser();

        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("name", user.getName());
            return "mypage";
        }
        // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }

    @GetMapping("/password")
    public String showPassword() {
        return "password";
    }

    @PostMapping("/password")
    public String password(Model model, String password) {
        String username = userService.getLoggedInUsername();

        Optional<UserEntity> userOptional = userService.validateUser(username, password);

        if (userOptional.isPresent()) {
            return "redirect:/update";
        } else {
            model.addAttribute("error", "비밀번호가 잘못 입력되었습니다.");
            return "password";
        }
    }


    @GetMapping("/update")
    public String showUpdate(Model model) {
        Long userId = userService.getLoggedInId();

        UserEntity user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam String name,
                             @RequestParam String username,
                             @RequestParam String password,
                             HttpSession session) {

        Long userId = userService.getLoggedInId();

        userService.updateUser(userId, name, username, password); // UserService로 수정 요청

        logout(session);
        return "redirect:/login"; // 수정 후 리디렉션
    }

}
