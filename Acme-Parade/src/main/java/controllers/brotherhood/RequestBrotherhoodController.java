
package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ParadeService;
import services.PlaceService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Request;

@Controller
@RequestMapping("/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	// Services

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private PlaceService		placeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		Collection<Parade> parades;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		requests = this.requestService.findAllByBrotherhood(brotherhood.getId());
		parades = this.paradeService.findAllFinal();

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("parades", parades);
		result.addObject("requestURI", "request/brotherhood/list.do");

		return result;

	}
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = {
		"requestStatus"
	})
	public ModelAndView listByStatus(@RequestParam final int requestStatus) {
		final ModelAndView result;
		Map<String, List<Request>> groupedRequest;
		Collection<Request> requests;
		Brotherhood principal;
		principal = this.brotherhoodService.findByPrincipal();

		requests = this.requestService.findAllByBrotherhood(principal.getId());

		groupedRequest = this.requestService.groupByStatus(requests);

		if (requestStatus == 0)
			requests = this.requestService.findAllByBrotherhood(principal.getId());
		else if (requestStatus == 1)
			requests = new ArrayList<Request>(groupedRequest.get("APPROVED"));
		else if (requestStatus == 2)
			requests = new ArrayList<Request>(groupedRequest.get("PENDING"));
		else if (requestStatus == 3)
			requests = new ArrayList<Request>(groupedRequest.get("REJECTED"));
		else
			requests = null;

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("requestURI", "request/brotherhood/list.do");

		return result;

	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int requestId) {
		final ModelAndView result;
		Request request;

		request = this.requestService.findOne(requestId);

		result = new ModelAndView("request/display");
		result.addObject("request", request);

		return result;
	}

	// REJECT

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		request = this.requestService.findOne(requestId);
		Assert.notNull(request);

		if (request.getParade().getBrotherhood().getId() == principal.getId() && request.getStatus().equals("PENDING"))
			result = this.createEditModelAndView(request, false);
		else
			result = new ModelAndView("redirect:list.do");

		return result;

	}
	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@ModelAttribute("request") Request request, final BindingResult binding) {
		ModelAndView result;

		try {
			request = this.requestService.reconstructReject(request, binding);
			Assert.isTrue(!(request.getRejectionReason().isEmpty()), "There must be a reason");
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(request, false);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				this.requestService.reject(request);
				request = this.requestService.save(request);
				result = new ModelAndView("redirect:list.do");
			}

		} catch (final Throwable oops) {
			if (oops.getMessage().contains("There must be a reason"))
				result = this.createEditModelAndView(request, "request.commit.error.rejectionReason", false);
			else
				result = this.createEditModelAndView(request, "request.commit.error", false);
		}
		return result;
	}

	// APPROVE

	@RequestMapping(value = "/approve", method = RequestMethod.GET)
	public ModelAndView approve(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		request = this.requestService.findOne(requestId);
		Assert.notNull(request);

		if (request.getParade().getBrotherhood().getId() == principal.getId() && request.getStatus().equals("PENDING"))
			result = this.createEditModelAndView(request, true);
		else
			result = new ModelAndView("redirect:list.do");

		return result;

	}
	@RequestMapping(value = "/approve", method = RequestMethod.POST, params = "save")
	public ModelAndView approve(@ModelAttribute("request") Request request, final BindingResult binding) {
		ModelAndView result;

		try {
			request = this.requestService.reconstructApprove(request, binding);
			Assert.isTrue(this.placeService.findRepeated(request.getParade().getId(), request.getPlace().getrowP(), request.getPlace().getcolumnP()) <= 0, "This place is busy");
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(request, true);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				this.requestService.approve(request);
				result = new ModelAndView("redirect:list.do");
			}

		} catch (final Throwable oops) {
			if (oops.getMessage().contains("This place is busy"))
				result = this.createEditModelAndView(request, "request.commit.error.busy", true);
			else
				result = this.createEditModelAndView(request, "request.commit.error", true);
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Request request, final Boolean approve) {
		ModelAndView result;

		result = this.createEditModelAndView(request, null, approve);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Request request, final String messageCode, final Boolean approve) {
		ModelAndView result;
		Parade parade;
		parade = this.paradeService.findOneByRequestId(request.getId());

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("parade", parade);
		result.addObject("message", messageCode);
		result.addObject("approve", approve);
		return result;
	}

}
