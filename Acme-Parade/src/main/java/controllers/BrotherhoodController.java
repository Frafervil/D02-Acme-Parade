
package controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import services.CustomisationService;
import services.FloatService;
import services.MemberService;
import services.ParadeService;
import domain.Brotherhood;
import domain.Member;
import domain.Parade;
import forms.BrotherhoodForm;

@Controller
@RequestMapping("/brotherhood")
public class BrotherhoodController extends AbstractController {

	// Services

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private FloatService			floatService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private CustomisationService	customisationService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.findAll();

		result = new ModelAndView("brotherhood/list");
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "brotherhood/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam(required = false) final Integer brotherhoodId) {
		final ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();

		if (brotherhoodId == null)
			brotherhood = this.brotherhoodService.findByPrincipal();
		else
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);

		Collection<Member> members;
		members = this.memberService.findAllMembersOfOneBrotherhood(brotherhood.getId());
		Collection<Parade> parades;
		parades = this.paradeService.findAllParadesOfOneBrotherhood(brotherhood.getId());
		Collection<domain.Float> floats;
		floats = this.floatService.findByBrotherhoodId(brotherhood.getId());

		result = new ModelAndView("brotherhood/display");
		result.addObject("brotherhood", brotherhood);
		result.addObject("parades", parades);
		result.addObject("members", members);
		result.addObject("floats", floats);

		return result;

	}

	//Create
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Brotherhood brotherhood;
		BrotherhoodForm brotherhoodForm;

		brotherhood = this.brotherhoodService.create();
		brotherhoodForm = this.brotherhoodService.construct(brotherhood);
		result = this.createEditModelAndView(brotherhoodForm);

		return result;
	}

	// Save de Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("brotherhood") Brotherhood brotherhood, final BindingResult binding) {
		ModelAndView result;

		try {
			brotherhood = this.brotherhoodService.reconstructPruned(brotherhood, binding);
			if (binding.hasErrors()) {
				result = this.editModelAndView(brotherhood);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else
				brotherhood = this.brotherhoodService.save(brotherhood);
			result = new ModelAndView("welcome/index");
		} catch (final Throwable oops) {
			result = this.editModelAndView(brotherhood, "brotherhood.commit.error");

		}

		return result;
	}

	//Save de Register
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView register(@ModelAttribute("brotherhoodForm") @Valid final BrotherhoodForm brotherhoodForm, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.reconstruct(brotherhoodForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(brotherhoodForm);
			} else
				brotherhood = this.brotherhoodService.save(brotherhood);
			result = new ModelAndView("welcome/index");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(brotherhoodForm, "brotherhood.commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood);
		result = this.editModelAndView(brotherhood);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	private ModelAndView editModelAndView(final Brotherhood brotherhood) {
		ModelAndView result;
		result = this.editModelAndView(brotherhood, null);
		return result;
	}

	private ModelAndView editModelAndView(final Brotherhood brotherhood, final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("brotherhood/edit");
		result.addObject("brotherhood", brotherhood);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);
		return result;
	}

	protected ModelAndView createEditModelAndView(final BrotherhoodForm brotherhoodForm) {
		ModelAndView result;
		result = this.createEditModelAndView(brotherhoodForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final BrotherhoodForm brotherhoodForm, final String message) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();
		if (brotherhoodForm.getId() != 0)
			result = new ModelAndView("brotherhood/edit");
		else
			result = new ModelAndView("brotherhood/register");

		result.addObject("brotherhoodForm", brotherhoodForm);
		result.addObject("actionURI", "brotherhood/edit.do");
		result.addObject("redirectURI", "welcome/index.do");
		result.addObject("areas", this.areaService.findAll());
		result.addObject("countryCode", countryCode);

		result.addObject("message", message);

		return result;
	}
}
