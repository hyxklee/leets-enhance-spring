package leets.enhance.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

    //test용 일반 컨트롤러
    @GetMapping("/")
    public String returnString(){
        return "Hello World";
    }
}
