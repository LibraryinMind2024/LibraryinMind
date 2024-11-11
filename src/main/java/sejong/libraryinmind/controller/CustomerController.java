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

    @RequestMapping("/login")
    //url과 customer 매핑
    public String list(Model model){

        List<CustomerEntity> customerEntityList = this.customerService.getList();
        model.addAttribute("customerEntityList", customerEntityList);

        return  "login";
    }

//    @RequestMapping("/")
//    public String root(){
//        return "redirect:/customer";
//    }

    @PostMapping("/login/create")
    public String customerCreate(@RequestParam String username, String password){
        this.customerService.create(username,password);

        return "redirect:/login";
    }

    @DeleteMapping("/customer/delete/{id}")
    public String customerDelete(@PathVariable Long id){
        this.customerService.delete(id);
        return "redirect:/customer";
    }


}
