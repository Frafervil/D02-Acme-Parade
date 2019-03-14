
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LinkRecordService;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord")
public class LinkRecordController extends AbstractController {

	// Servicios

	@Autowired
	private LinkRecordService	linkRecordService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int linkRecordId) {
		// Inicializa resultado
		ModelAndView result;
		LinkRecord linkRecord;

		// Busca en el repositorio
		linkRecord = this.linkRecordService.findOne(linkRecordId);
		Assert.notNull(linkRecordId);

		// Crea y a�ade objetos a la vista
		result = new ModelAndView("linkRecord/display");
		result.addObject("requestURI", "linkRecord/display.do");
		result.addObject("linkRecord", linkRecord);

		// Env�a la vista
		return result;
	}
}
