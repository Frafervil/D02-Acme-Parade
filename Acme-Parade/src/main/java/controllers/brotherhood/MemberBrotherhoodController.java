package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import services.MemberService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/member/brotherhood")
public class MemberBrotherhoodController extends AbstractController {

	// Servicios

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private EnrolmentService enrolmentService;

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Member> members;
		try {
			final Brotherhood hood = this.brotherhoodService
					.findOne(brotherhoodId);
			Assert.notNull(hood);

			members = this.memberService
					.findAllActiveMembersOfOneBrotherhood(brotherhoodId);
			result = new ModelAndView("member/brotherhood/list");
			result.addObject("members", members);
			result.addObject("requestURI", "member/brotherhood/list.do");

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = new ModelAndView("member/brotherhood/list");
			result.addObject("message", "member.retrieve.error");
			result.addObject("members", new ArrayList<Member>());
		}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int memberId) {
		// Inicializa resultado
		ModelAndView result;
		Member member;
		Brotherhood principal;
		Enrolment enrolment;

		// Busca en el repositorio
		member = this.memberService.findOne(memberId);
		Assert.notNull(member);

		principal = this.brotherhoodService.findByPrincipal();

		enrolment = this.enrolmentService
				.findActiveEnrolmentByBrotherhoodIdAndMemberId(
						principal.getId(), memberId);

		// Crea y añade objetos a la vista
		result = new ModelAndView("member/display");
		result.addObject("requestURI", "member/display.do");
		result.addObject("enrolment", enrolment);
		result.addObject("member", member);

		// Envía la vista
		return result;
	}

}
