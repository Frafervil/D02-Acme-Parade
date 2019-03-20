
package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Controller
@RequestMapping("/history/brotherhood")
public class HistoryBrotherhoodController extends AbstractController {

	//Services
	@Autowired
	private HistoryService		historyService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		// Inicializa resultado
		ModelAndView result;
		History history;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		// Busca en el repositorio
		history = this.historyService.findByBrotherhoodId(principal.getId());
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
