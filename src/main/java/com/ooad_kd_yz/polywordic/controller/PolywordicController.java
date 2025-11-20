package com.ooad_kd_yz.polywordic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolywordicController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
