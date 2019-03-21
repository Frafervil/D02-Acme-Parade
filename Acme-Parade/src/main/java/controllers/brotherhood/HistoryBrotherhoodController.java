
package controllers.brotherhood;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import services.InceptionRecordService;
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
	private HistoryService			historyService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		// Inicializa resultado
		final ModelAndView result;
		History history;
		Brotherhood principal;
		Collection<PeriodRecord> pRs = null;
		Collection<LegalRecord> lRs = null;
		Collection<LinkRecord> lkRs = null;
		Collection<MiscellaneousRecord> mRs = null;

		principal = this.brotherhoodService.findByPrincipal();

		// Busca en el repositorio
		history = this.historyService.findByBrotherhoodId(principal.getId());

		if (history != null) {
			pRs = history.getPeriodRecords();
			lRs = history.getLegalRecords();
			lkRs = history.getLinkRecords();
			mRs = history.getMiscellaneousRecords();
		}
		// Crea y añade objetos a la vista
		result = new ModelAndView("history/display");
		result.addObject("requestURI", "history/display.do");
		result.addObject("history", history);
		if (history != null) {
			result.addObject("periodRecords", pRs);
			result.addObject("legalRecords", lRs);
			result.addObject("linkRecords", lkRs);
			result.addObject("miscellaneousRecords", mRs);
		}
		// Envía la vista
		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final History history;

		history = this.historyService.create();

		result = this.createEditModelAndView(history);

		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		History history;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		history = this.historyService.findByBrotherhoodId(principal.getId());
		Assert.notNull(history);
		result = this.createEditModelAndView(history);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final History history, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(history);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.inceptionRecordService.save(history.getInceptionRecord());
				this.historyService.save(history);
				result = new ModelAndView("redirect:display.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(history, "history.commit.error");
			}
		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final History history) {
		ModelAndView result;

		result = this.createEditModelAndView(history, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final History history, final String message) {
		ModelAndView result;

		result = new ModelAndView("history/edit");
		result.addObject("history", history);
		result.addObject("message", message);

		return result;
	}

}
