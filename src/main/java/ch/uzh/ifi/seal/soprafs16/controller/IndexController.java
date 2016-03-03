package ch.uzh.ifi.seal.soprafs16.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	@RequestMapping(value="/")
	@ResponseBody
	public String index() {
		return "SoPra 2016 with Codeship!";
	}
}
