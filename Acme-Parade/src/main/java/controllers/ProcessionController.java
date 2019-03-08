package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repositories.FloatRepository;
import services.ProcessionService;
import domain.Procession;

@Controller
@RequestMapping("/procession")
public class ProcessionController extends AbstractController {

	// Servicios

	@Autowired
	private ProcessionService processionService;

	// Repositorios

	@Autowired
	private FloatRepository floatRepository;

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int processionId) {
		// Inicializa resultado
		ModelAndView result;
		Procession procession;
		Collection<domain.Float> floats;

		// Busca en el repositorio
		procession = this.processionService.findOne(processionId);
		Assert.notNull(procession);

		floats = this.floatRepository.findByProcessionId(processionId);
		Assert.notNull(floats);

		// Crea y añade objetos a la vista
		result = new ModelAndView("procession/display");
		result.addObject("requestURI", "procession/display.do");
		result.addObject("procession", procession);
		result.addObject("floats", floats);

		// Envía la vista
		return result;
	}

}
