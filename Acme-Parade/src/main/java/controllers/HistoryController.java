
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HistoryService;
import domain.History;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Controller
@RequestMapping("/history")
public class HistoryController extends AbstractController {

	// Servicios

	@Autowired
	private HistoryService	historyService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<History> histories;

		try {
			histories = this.historyService.findAll();

			result = new ModelAndView("history/list");
			result.addObject("histories", histories);
			result.addObject("requestURI", "history/list.do");

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = new ModelAndView("history/list");
			result.addObject("message", "history.retrieve.error");
			result.addObject("members", new ArrayList<History>());
		}

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int historyId) {
		// Inicializa resultado
		ModelAndView result;
		History history;

		// Busca en el repositorio
		history = this.historyService.findOne(historyId);
		Assert.notNull(history);

		final Collection<PeriodRecord> pRs = history.getPeriodRecords();
		final Collection<LegalRecord> lRs = history.getLegalRecords();
		final Collection<LinkRecord> lkRs = history.getLinkRecords();
		final Collection<MiscellaneousRecord> mRs = history.getMiscellaneousRecords();

		// Crea y añade objetos a la vista
		result = new ModelAndView("history/display");
		result.addObject("requestURI", "history/display.do");
		result.addObject("history", history);
		result.addObject("periodRecords", pRs);
		result.addObject("legalRecords", lRs);
		result.addObject("linkRecords", lkRs);
		result.addObject("miscellaneousRecords", mRs);

		// Envía la vista
		return result;
	}
}
