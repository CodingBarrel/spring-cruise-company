package ua.cruise.springcruise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {
    @GetMapping("{name}")
    public String hello(@PathVariable("name") String name, Model model) {
        model.addAttribute("message", "Hello, " + name + "!");
        return "hello";
    }
}
