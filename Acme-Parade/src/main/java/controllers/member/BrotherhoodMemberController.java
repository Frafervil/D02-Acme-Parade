
package controllers.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EnrolmentService;
import controllers.AbstractController;

@Controller
@RequestMapping("/brotherhood/member")
public class BrotherhoodMemberController extends AbstractController {

	// Servicios

	@Autowired
	private EnrolmentService	enrolmentService;


	// Drop Out

	@RequestMapping(value = "/dropOut", method = RequestMethod.GET)
	public ModelAndView enrol(@RequestParam final int brotherhoodId) {
		final ModelAndView result;

		this.enrolmentService.dropOut(brotherhoodId);

		result = new ModelAndView("redirect:/enrolment/member/list.do");

		return result;
	}

}
