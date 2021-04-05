package com.sbs.untact.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class mpaUsrHomeController {
	@RequestMapping("/mpaUsr/home/main")
	public String showMain() {
		return "/mpaUsr/home/main";
	}
	
	@RequestMapping("/")
	public String showMainRoot() {
		return "redirect:/mpaUsr/home/main";
	}
}
