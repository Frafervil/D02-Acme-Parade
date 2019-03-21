
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
import services.LinkRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class LinkRecordBrotherhoodController extends AbstractController {

	//Services
	@Autowired
	private HistoryService		historyService;

	@Autowired
	private LinkRecordService	linkRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LinkRecord linkRecord;
		Brotherhood principal;
		History history;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		linkRecord = this.linkRecordService.create(history);

		result = this.createEditModelAndView(linkRecord);

		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord linkRecord;
		Brotherhood principal;
		History history;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		linkRecord = this.linkRecordService.findOne(linkRecordId);
		Assert.notNull(linkRecord);

		Assert.isTrue(history.getLinkRecords().contains(linkRecord));

		result = this.createEditModelAndView(linkRecord);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecord linkRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(linkRecord);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.linkRecordService.save(linkRecord);
				result = new ModelAndView("redirect:/history/brotherhood/display.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(linkRecord, "history.commit.error");
			}
		return result;
	}

	// Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord linkRecord;
		Brotherhood principal;
		History history;

		linkRecord = this.linkRecordService.findOne(linkRecordId);
		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		if (history.getLinkRecords().contains(linkRecord))
			try {
				this.linkRecordService.delete(linkRecord);
				result = new ModelAndView("redirect:/history/brotherhood/display.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(linkRecord, "linkRecord.commit.error");

			}
		else
			result = this.createEditModelAndView(linkRecord, "linkRecord.commit.error");

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(linkRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecord", linkRecord);
		result.addObject("message", message);

		return result;
	}

}
