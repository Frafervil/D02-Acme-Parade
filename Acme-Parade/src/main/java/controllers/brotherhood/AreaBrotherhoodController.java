
package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import controllers.AbstractController;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping("/area/brotherhood")
public class AreaBrotherhoodController extends AbstractController {

	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Area> areas;
		final Brotherhood principal;
		boolean selected = false;

		areas = new ArrayList<Area>();
		principal = this.brotherhoodService.findByPrincipal();

		if (principal.getArea() != null) {
			areas.add(principal.getArea());
			selected = true;
		} else
			areas = this.areaService.findAreasWithNoBrotherhood();

		result = new ModelAndView("area/list");
		result.addObject("areas", areas);
		result.addObject("selected", selected);
		result.addObject("requestURI", "area/brotherhood/list.do");

		return result;
	}
}
