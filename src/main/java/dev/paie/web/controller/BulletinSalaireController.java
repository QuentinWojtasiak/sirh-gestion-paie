package dev.paie.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dev.paie.repository.BulletinSalairerepository;
import dev.paie.services.EmployeService;

@Controller
@RequestMapping("/bulletins")
public class BulletinSalaireController {

	@Autowired
	private EmployeService employeService;

	@Autowired
	BulletinSalairerepository bulletinSalairerepository;

	@RequestMapping(method = RequestMethod.GET, path = "/creerEmploye")
	public ModelAndView creerEmploye() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("employes/creerEmploye");
		mv.addObject("employes", employeService.getLesEmploye());
		return mv;
	}

}
