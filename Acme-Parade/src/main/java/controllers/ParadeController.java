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
import services.ParadeService;
import domain.Parade;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	// Servicios

	@Autowired
	private ParadeService paradeService;

	// Repositorios

	@Autowired
	private FloatRepository floatRepository;

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		// Inicializa resultado
		ModelAndView result;
		Parade parade;
		Collection<domain.Float> floats;

		// Busca en el repositorio
		parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);

		floats = this.floatRepository.findByParadeId(paradeId);
		Assert.notNull(floats);

		// Crea y añade objetos a la vista
		result = new ModelAndView("parade/display");
		result.addObject("requestURI", "parade/display.do");
		result.addObject("parade", parade);
		result.addObject("floats", floats);

		// Envía la vista
		return result;
	}

}
