
package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Procession;
import domain.Request;

@Controller
@RequestMapping("/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	// Services

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		Collection<Procession> processions;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		requests = this.requestService.findAllByBrotherhood(brotherhood.getId());
		processions = this.processionService.findAllFinal();

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("processions", processions);
		result.addObject("requestURI", "request/brotherhood/list.do");

		return result;

	}
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = {
		"requestStatus"
	})
	public ModelAndView listByStatus(@RequestParam final int requestStatus) {
		final ModelAndView result;
		Map<String, List<Request>> groupedRequest;
		final Collection<Request> requests;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		groupedRequest = this.requestService.groupByStatus(this.requestService.findAllByBrotherhood(brotherhood.getId()));

		if (requestStatus == 0)
			requests = this.requestService.findAllByBrotherhood(brotherhood.getId());
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

		if (request.getProcession().getBrotherhood().getId() == principal.getId() && request.getStatus().equals("PENDING"))
			result = this.createEditModelAndView(request, false);
		else
			result = new ModelAndView("redirect:list.do");

		return result;

	}
	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final Request request, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(request, false);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.requestService.reject(request);
				this.requestService.save(request);

				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
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

		if (request.getProcession().getBrotherhood().getId() == principal.getId() && request.getStatus().equals("PENDING"))
			result = this.createEditModelAndView(request, true);
		else
			result = new ModelAndView("redirect:list.do");

		return result;

	}
	@RequestMapping(value = "/approve", method = RequestMethod.POST, params = "save")
	public ModelAndView approve(@Valid final Request request, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(request, true);
			System.out.println(binding.getAllErrors());
		} else

			try {
				this.requestService.approve(request);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
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

	protected ModelAndView createEditModelAndView(final Request request, final String message, final Boolean approve) {
		ModelAndView result;
		boolean permission = false;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		if (request.getProcession().getBrotherhood().getId() == principal.getId())
			permission = true;

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("permission", permission);
		result.addObject("message", message);
		result.addObject("approve", approve);
		return result;
	}

}
