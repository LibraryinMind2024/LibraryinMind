package sejong.libraryinmind.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sejong.libraryinmind.entity.CustomerEntity;
import sejong.libraryinmind.service.CustomerService;

import java.util.List;

@RequiredArgsConstructor
//CustomerRepository 속성을 포함하는 생성자를 자동으로 생성하는 것
@Controller
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping("/customer")
    //url과 customer 매핑
    public String list(Model model){

        List<CustomerEntity> customerEntityList = this.customerService.getList();
        model.addAttribute("customerEntityList", customerEntityList);

        return  "Customer";
    }

    @RequestMapping("/")
    public String root(){
        return "main";
    }

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {
        // 로그인된 사용자 이름 가져오기
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return "main"; // main.html 반환
        }
        // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }

    //회원가입 페이지 이동
    @RequestMapping("/signup")
    public String signup(Model model){

        List<CustomerEntity> customerEntityList = this.customerService.getList();
        model.addAttribute("customerEntityList", customerEntityList);

        return  "signup";
    }

    //회원가입 처리
    @PostMapping("/signup/create")
    public String customerCreate(@RequestParam String username, String password){
        this.customerService.create(username,password);

        return "redirect:/signup";
    }


    // 로그인 페이지 이동
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }


    // 로그인 처리
    @PostMapping("/login")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if (customerService.validateUser(username, password)) {
            // 로그인 성공 시 세션에 사용자 이름 저장
            session.setAttribute("username", username);

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


    @DeleteMapping("/customer/delete/{id}")
    public String customerDelete(@PathVariable Long id){
        this.customerService.delete(id);
        return "redirect:/customer";
    }

}
