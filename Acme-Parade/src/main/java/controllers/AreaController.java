
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping("/area")
public class AreaController extends AbstractController {

	// Servicios

	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int areaId) {
		ModelAndView result;
		final Area area;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByAreaId(areaId);

		// Busca en el repositorio
		area = this.areaService.findOne(areaId);
		Assert.notNull(area);

		// Crea y añade objetos a la vista
		result = new ModelAndView("area/display");
		result.addObject("requestURI", "area/display.do");
		result.addObject("area", area);
		result.addObject("brotherhood", brotherhood);

		// Envía la vista
		return result;
	}
}
