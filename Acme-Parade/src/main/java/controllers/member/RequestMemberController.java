
package controllers.member;

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

import services.MemberService;
import services.PlaceService;
import services.ParadeService;
import services.RequestService;
import controllers.AbstractController;
import domain.Member;
import domain.Parade;
import domain.Request;

@Controller
@RequestMapping("/request/member")
public class RequestMemberController extends AbstractController {

	// Services

	@Autowired
	private RequestService		requestService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private ParadeService	paradeService;

	@Autowired
	private PlaceService		placeService;


	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		Collection<Parade> parades;
		Member principal;

		principal = this.memberService.findByPrincipal();
		requests = this.requestService.findByPrincipal();
		parades = this.paradeService.findAllAvailableRequest(principal.getId());

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("parades", parades);
		result.addObject("requestURI", "request/member/list.do");

		return result;

	}
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = {
		"requestStatus"
	})
	public ModelAndView listByStatus(@RequestParam final int requestStatus) {
		final ModelAndView result;
		Map<String, List<Request>> groupedRequest;
		final Collection<Request> requests;
		Collection<Parade> parades;
		Member principal;

		principal = this.memberService.findByPrincipal();
		parades = this.paradeService.findAllAvailableRequest(principal.getId());

		groupedRequest = this.requestService.groupByStatus(this.requestService.findByPrincipal());

		if (requestStatus == 0)
			requests = this.requestService.findByPrincipal();
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
		result.addObject("parades", parades);
		result.addObject("requestURI", "request/member/list.do");

		return result;

	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int requestId) {
		final ModelAndView result;
		Request request;
		Member member;
		boolean permission = false;

		request = this.requestService.findOne(requestId);
		member = this.memberService.findByPrincipal();

		if (this.requestService.findAllByMember(member.getId()).contains(request))
			permission = true;

		result = new ModelAndView("request/display");
		result.addObject("request", request);
		result.addObject("member", member);
		result.addObject("permission", permission);

		return result;
	}

	// Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		Member member;
		request = this.requestService.findOne(requestId);
		member = this.memberService.findByPrincipal();
		if (request.getMember().getId() == member.getId())
			try {
				this.requestService.delete(request);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(request, "request.commit.error");
				result.addObject("permission", true);

			}
		else
			result = this.createEditModelAndView(request, "request.commit.error");

		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		final Request request;

		request = this.requestService.create(paradeId);

		result = this.createEditModelAndView(request);

		return result;

	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		request = this.requestService.findOne(requestId);
		Assert.notNull(request);
		result = this.createEditModelAndView(request);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Request request, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(request);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.placeService.save(request.getPlace());
				this.placeService.flushPlace();
				this.requestService.save(request);
				this.requestService.flushRequest();
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Request request) {
		ModelAndView result;

		result = this.createEditModelAndView(request, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Request request, final String message) {
		ModelAndView result;

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("message", message);

		return result;
	}
}
