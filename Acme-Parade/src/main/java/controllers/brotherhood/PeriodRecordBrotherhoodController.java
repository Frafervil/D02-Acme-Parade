
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
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class PeriodRecordBrotherhoodController extends AbstractController {

	//Services
	@Autowired
	private HistoryService		historyService;

	@Autowired
	private PeriodRecordService	periodRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PeriodRecord periodRecord;
		Brotherhood principal;
		History history;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		periodRecord = this.periodRecordService.create(history);

		result = this.createEditModelAndView(periodRecord);

		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId) {
		ModelAndView result;
		PeriodRecord periodRecord;
		Brotherhood principal;
		History history;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		periodRecord = this.periodRecordService.findOne(periodRecordId);
		Assert.notNull(periodRecord);

		Assert.isTrue(history.getPeriodRecords().contains(periodRecord));

		result = this.createEditModelAndView(periodRecord);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(periodRecord);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView("redirect:/history/brotherhood/display.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(periodRecord, "history.commit.error");
			}
		return result;
	}

	// Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int periodRecordId) {
		ModelAndView result;
		PeriodRecord periodRecord;
		Brotherhood principal;
		History history;

		periodRecord = this.periodRecordService.findOne(periodRecordId);
		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		if (history.getPeriodRecords().contains(periodRecord))
			try {
				this.periodRecordService.delete(periodRecord);
				result = new ModelAndView("redirect:/history/brotherhood/display.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");

			}
		else
			result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(periodRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecord", periodRecord);
		result.addObject("message", message);

		return result;
	}

}
