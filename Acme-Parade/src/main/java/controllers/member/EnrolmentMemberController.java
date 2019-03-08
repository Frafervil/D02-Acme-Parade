
package controllers.member;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EnrolmentService;
import services.MemberService;
import controllers.AbstractController;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/enrolment/member")
public class EnrolmentMemberController extends AbstractController {

	// Servicios

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private MemberService		memberService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Enrolment> enrolments;

		try {
			final Member principal = this.memberService.findByPrincipal();
			Assert.notNull(principal);

			enrolments = this.enrolmentService.findByMemberId(principal.getId());

			result = new ModelAndView("enrolment/member/list");
			result.addObject("enrolments", enrolments);
			result.addObject("requestURI", "enrolment/member/list.do");

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = new ModelAndView("enrolment/member/list");
			result.addObject("message", "enrolment.retrieve.error");
			result.addObject("enrolments", new ArrayList<Enrolment>());
		}

		return result;
	}

}
