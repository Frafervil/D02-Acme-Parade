
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FloatService;

@Controller
@RequestMapping("/float")
public class FloatController extends AbstractController {

	// Servicios

	@Autowired
	private FloatService	floatService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int floatId) {
		// Inicializa resultado
		ModelAndView result;
		domain.Float floatB;

		// Busca en el repositorio
		floatB = this.floatService.findOne(floatId);
		Assert.notNull(floatB);

		// Crea y añade objetos a la vista
		result = new ModelAndView("float/display");
		result.addObject("requestURI", "float/display.do");
		result.addObject("floatB", floatB);

		// Envía la vista
		return result;
	}

}
