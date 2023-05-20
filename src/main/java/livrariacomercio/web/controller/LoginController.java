package livrariacomercio.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@RequestMapping(method = RequestMethod.GET, value ="/login")
	public ModelAndView indexlogin() {
		ModelAndView andView = new ModelAndView("login");
		return andView;
	}

}
