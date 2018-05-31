package dev.paie.web.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import dev.paie.entite.Collegue;
import dev.paie.entite.Entreprise;
import dev.paie.entite.Grade;
import dev.paie.entite.ProfilRemuneration;
import dev.paie.entite.RemunerationEmploye;
import dev.paie.services.EmployeService;
import dev.paie.services.EntrepriseService;
import dev.paie.services.GradeServiceJdbcTemplate;
import dev.paie.services.ProfilService;

@Controller
@RequestMapping("/employes")
public class RemunerationEmployeController {

	@Autowired
	private EntrepriseService entrepriseService;

	@Autowired
	private ProfilService profilService;

	@Autowired
	private GradeServiceJdbcTemplate gradeServiceJdbcTemplate;

	@Autowired
	private EmployeService employeService;

	@RequestMapping(method = RequestMethod.GET, path = "/creerEmploye")
	@Secured("ROLE_ADMINISTRATEUR")
	public ModelAndView creerEmploye() {
		List<Grade> grades = gradeServiceJdbcTemplate.lister();
		List<ProfilRemuneration> profils = profilService.getLesProfil();
		List<Entreprise> lesEntre = entrepriseService.getLesEntreprises();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("employes/creerEmploye");
		mv.addObject("lesEntre", lesEntre);
		mv.addObject("profils", profils);
		mv.addObject("grades", grades);
		mv.addObject("employe", new RemunerationEmploye());
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/creerEmploye")
	@Secured("ROLE_ADMINISTRATEUR")
	public ModelAndView submitForm(@ModelAttribute("employe") RemunerationEmploye emp, BindingResult result) {
		RestTemplate rt = new RestTemplate();
		Collegue[] lesC = rt.getForObject("http://collegues-api.cleverapps.io/collegues", Collegue[].class);
		emp.setDateCreation(LocalDateTime.now());
		for (int i = 0; i < lesC.length; i++) {
			if (lesC[i].getMatricule().equals(emp.getMatricule())) {
				employeService.saveEmp(emp);
				return new ModelAndView("redirect:/mvc/employes/listeEmploye");
			}
		}

		result.rejectValue("matricule", "error.matricule", "Le matricule n'existe pas");
		List<Grade> grades = gradeServiceJdbcTemplate.lister();
		List<ProfilRemuneration> profils = profilService.getLesProfil();
		List<Entreprise> lesEntre = entrepriseService.getLesEntreprises();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("employes/creerEmploye");
		mv.addObject("lesEntre", lesEntre);
		mv.addObject("profils", profils);
		mv.addObject("grades", grades);
		// mv.addObject("employe", new RemunerationEmploye());
		return mv;
		// return creerEmploye();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/listeEmploye")
	@Secured({ "ROLE_UTILISATEUR", "ROLE_ADMINISTRATEUR" })
	public ModelAndView listerEmploye() {
		List<RemunerationEmploye> employes = employeService.getLesEmploye();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("employes/listeEmploye");
		mv.addObject("employes", employes);
		return mv;
	}

}