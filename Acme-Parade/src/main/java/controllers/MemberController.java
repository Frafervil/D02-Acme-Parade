
package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import services.MemberService;
import domain.Member;
import forms.MemberForm;

@Controller
@RequestMapping("/member")
public class MemberController extends AbstractController {

	// Services
	@Autowired
	private MemberService			memberService;

	@Autowired
	private CustomisationService	customisationService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Member> members;

		try {
			members = this.memberService.findAll();

			result = new ModelAndView("member/list");
			result.addObject("members", members);
			result.addObject("requestURI", "member/list.do");

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = new ModelAndView("member/list");
			result.addObject("message", "member.retrieve.error");
			result.addObject("members", new ArrayList<domain.Float>());
		}

		return result;
	}

	// Create
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Member member;
		MemberForm memberForm;

		member = this.memberService.create();
		memberForm = this.memberService.construct(member);
		result = this.createEditModelAndView(memberForm);

		return result;
	}

	// Save de Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("member") Member member, final BindingResult binding) {
		ModelAndView result;

		try {
			member = this.memberService.reconstructPruned(member, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				result = this.editModelAndView(member);
			} else {
				member = this.memberService.save(member);
				result = new ModelAndView("welcome/index");
			}
		} catch (final Throwable oops) {
			result = this.editModelAndView(member, "member.commit.error");
		}

		return result;
	}

	private ModelAndView editModelAndView(final Member member) {
		ModelAndView result;
		result = this.editModelAndView(member, null);
		return result;
	}

	private ModelAndView editModelAndView(final Member member, final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("member/edit");
		result.addObject("member", member);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);
		return result;
	}

	// Save de register o create
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView register(@ModelAttribute("memberForm") @Valid final MemberForm memberForm, final BindingResult binding) {
		ModelAndView result;
		Member member;

		try {
			member = this.memberService.reconstruct(memberForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(memberForm);
			} else {
				member = this.memberService.save(member);
				result = new ModelAndView("welcome/index");
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(memberForm, "member.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView view() {
		ModelAndView result;

		result = new ModelAndView("member/display");
		result.addObject("member", this.memberService.findByPrincipal());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Member member;

		member = this.memberService.findByPrincipal();
		Assert.notNull(member);
		result = this.editModelAndView(member);

		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete() {
		ModelAndView result;

		try {
			this.memberService.delete();

			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/member/display.do");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final MemberForm memberForm) {
		ModelAndView result;
		result = this.createEditModelAndView(memberForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MemberForm memberForm, final String message) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();
		if (memberForm.getIdMember() != 0)
			result = new ModelAndView("member/edit");
		else
			result = new ModelAndView("member/register");
		result.addObject("memberForm", memberForm);
		result.addObject("redirectURI", "welcome/index.do");
		result.addObject("countryCode", countryCode);

		result.addObject("message", message);

		return result;
	}
}
