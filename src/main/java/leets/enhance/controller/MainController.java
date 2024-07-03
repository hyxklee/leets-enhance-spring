package leets.enhance.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class MainController {
    @GetMapping("/main")
    public String hello(){
        return "Hello World!";
    }
}
