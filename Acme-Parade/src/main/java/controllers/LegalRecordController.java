
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LegalRecordService;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord")
public class LegalRecordController extends AbstractController {

	// Servicios

	@Autowired
	private LegalRecordService	legalRecordService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int legalRecordId) {
		// Inicializa resultado
		ModelAndView result;
		LegalRecord legalRecord;

		// Busca en el repositorio
		legalRecord = this.legalRecordService.findOne(legalRecordId);
		Assert.notNull(legalRecordId);

		// Crea y añade objetos a la vista
		result = new ModelAndView("legalRecord/display");
		result.addObject("requestURI", "legalRecord/display.do");
		result.addObject("legalRecord", legalRecord);

		// Envía la vista
		return result;
	}
}
