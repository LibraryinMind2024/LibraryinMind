package sejong.libraryinmind.controller;

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

//    @RequestMapping("/")
//    public String root(){
//        return "redirect:/customer";
//    }

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
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        if (customerService.validateUser(username, password)) {
            model.addAttribute("username", username);
            return "main";  // 로그인 성공 시 환영 페이지로 이동
        } else {
            model.addAttribute("error", "아이디 또는 패스워드가 잘못 입력되었습니다.");
            return "login";
        }
    }

    @DeleteMapping("/customer/delete/{id}")
    public String customerDelete(@PathVariable Long id){
        this.customerService.delete(id);
        return "redirect:/customer";
    }


}
