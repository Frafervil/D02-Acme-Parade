
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import services.LegalRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class LegalRecordBrotherhoodController extends AbstractController {

	//Services
	@Autowired
	private HistoryService		historyService;

	@Autowired
	private LegalRecordService	legalRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LegalRecord legalRecord;
		Brotherhood principal;
		History history;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		legalRecord = this.legalRecordService.create(history);

		result = this.createEditModelAndView(legalRecord);

		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord;
		Brotherhood principal;
		History history;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		legalRecord = this.legalRecordService.findOne(legalRecordId);
		Assert.notNull(legalRecord);

		Assert.isTrue(history.getLegalRecords().contains(legalRecord));

		result = this.createEditModelAndView(legalRecord);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(legalRecord);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.legalRecordService.save(legalRecord);
				result = new ModelAndView("redirect:/history/brotherhood/display.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "history.commit.error");
			}
		return result;
	}

	// Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord;
		Brotherhood principal;
		History history;

		legalRecord = this.legalRecordService.findOne(legalRecordId);
		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		if (history.getLegalRecords().contains(legalRecord))
			try {
				this.legalRecordService.delete(legalRecord);
				result = new ModelAndView("redirect:/history/brotherhood/display.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");

			}
		else
			result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(legalRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecord", legalRecord);
		result.addObject("message", message);

		return result;
	}

}
