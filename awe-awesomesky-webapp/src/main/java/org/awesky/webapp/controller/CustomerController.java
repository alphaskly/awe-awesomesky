package org.awesky.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomerController {


	@RequestMapping(value="/toLogin")
	public String toLogin() {
		return "jsp/login";
	}
	
}
