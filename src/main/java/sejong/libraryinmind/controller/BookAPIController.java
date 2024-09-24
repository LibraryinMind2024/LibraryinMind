package sejong.libraryinmind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookAPIController {

    @GetMapping("/list")
    public String list(){
        System.out.println("hiiiiii");
        return "list";
    }

}
