
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MiscellaneousRecordService;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord")
public class MiscellaneousRecordController extends AbstractController {

	// Servicios

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int miscellaneousRecordId) {
		// Inicializa resultado
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;

		// Busca en el repositorio
		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		Assert.notNull(miscellaneousRecordId);

		// Crea y añade objetos a la vista
		result = new ModelAndView("miscellaneousRecord/display");
		result.addObject("requestURI", "miscellaneousRecord/display.do");
		result.addObject("miscellaneousRecord", miscellaneousRecord);

		// Envía la vista
		return result;
	}
}
