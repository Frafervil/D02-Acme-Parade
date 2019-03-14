
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PeriodRecordService;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord")
public class PeriodRecordController extends AbstractController {

	// Servicios

	@Autowired
	private PeriodRecordService	periodRecordService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int periodRecordId) {
		// Inicializa resultado
		ModelAndView result;
		PeriodRecord periodRecord;

		// Busca en el repositorio
		periodRecord = this.periodRecordService.findOne(periodRecordId);
		Assert.notNull(periodRecordId);

		// Crea y añade objetos a la vista
		result = new ModelAndView("periodRecord/display");
		result.addObject("requestURI", "periodRecord/display.do");
		result.addObject("periodRecord", periodRecord);

		// Envía la vista
		return result;
	}
}
