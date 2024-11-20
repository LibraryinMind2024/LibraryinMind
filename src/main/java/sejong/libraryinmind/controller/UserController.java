package sejong.libraryinmind.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.service.UserService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
//CustomerRepository 속성을 포함하는 생성자를 자동으로 생성하는 것
@Controller
public class UserController {

    private final UserService userService;

    @RequestMapping("/customer")
    //url과 customer 매핑
    public String list(Model model){

        List<UserEntity> userEntityList = this.userService.getList();
        model.addAttribute("customerEntityList", userEntityList);

        return  "Customer";
    }

    @RequestMapping("/")
    public String root(Model model, HttpSession session){
        // 로그인된 사용자 이름 가져오기
        String username = (String) session.getAttribute("username");
        String name = (String) session.getAttribute("name");

        if (username != null && name != null) {
            model.addAttribute("username", username);
            model.addAttribute("name", name);
        }
        return "main";
    }

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {
        // 로그인된 사용자 이름 가져오기
        String username = (String) session.getAttribute("username");
        String name = (String) session.getAttribute("name");

        if (username != null && name != null) {
            model.addAttribute("username", username);
            model.addAttribute("name", name);
        }
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
    public String customerCreate(@RequestParam String name, String username, String password){
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
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Optional<UserEntity> userOptional = userService.validateUser(username, password);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // 로그인 성공 시 세션에 사용자 이름과 name 저장
            session.setAttribute("username", username);
            session.setAttribute("name", user.getName());
            session.setAttribute("id", user.getId());

            // 환영 페이지로 이동
            return "redirect:/main";
        } else {
            // 로그인 실패 시 에러 메시지 전달
            model.addAttribute("error", "아이디 또는 패스워드가 잘못 입력되었습니다.");
            return "login";
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

    @GetMapping("/update")
    public String showUpdate() {
        return "update";
    }


    @GetMapping("/mypage")
    public String showMypage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String name = (String) session.getAttribute("name");

        if (username != null && name != null) {
            model.addAttribute("username", username);
            model.addAttribute("name", name);

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
    public String password(Model model, HttpSession session, String password) {
        String username = (String) session.getAttribute("username");

        Optional<UserEntity> userOptional = userService.validateUser(username, password);

        if (userOptional.isPresent()) {
            return "redirect:/update";
        } else {
            model.addAttribute("error", "비밀번호가 잘못 입력되었습니다.");
            return "password";
        }
    }

}
