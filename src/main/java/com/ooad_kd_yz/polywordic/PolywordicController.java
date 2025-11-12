package com.ooad_kd_yz.polywordic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolywordicController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Polywordic");
        model.addAttribute("message", "Welcome to Wordle in Java!");
        return "index"; // looks for templates/index.html
    }
}
