package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
import services.FloatService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Float;

@Controller
@RequestMapping("/float/brotherhood")
public class FloatBrotherhoodController extends AbstractController {

	// Servicios

	@Autowired
	private FloatService floatService;

	@Autowired
	private BrotherhoodService brotherhoodService;
	
	@Autowired
	private ParadeService paradeService;

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<domain.Float> floats;

		try {
			final Brotherhood hood = this.brotherhoodService
					.findOne(brotherhoodId);
			Assert.notNull(hood);

			floats = this.floatService.findByBrotherhoodId(brotherhoodId);

			result = new ModelAndView("float/list");
			result.addObject("floats", floats);
			result.addObject("requestURI", "float/brotherhood/list.do");

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = new ModelAndView("float/list");
			result.addObject("message", "float.retrieve.error");
			result.addObject("floats", new ArrayList<domain.Float>());
		}

		return result;
	}

	// Create

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		domain.Float floatB;

		floatB = this.floatService.create();

		result = this.createEditModelAndView(floatB);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		ModelAndView result;

		try {
			final domain.Float f = this.floatService.findOne(floatId);

			result = this.editModelAndView(f);

		} catch (final Exception oops) {
			oops.printStackTrace();
			result = new ModelAndView("welcome/index");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("float") domain.Float floatB,
			final BindingResult binding) {
		ModelAndView result;

		try {
			floatB = this.floatService.reconstruct(floatB, binding);
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				result = this.editModelAndView(floatB);
			} else {
				floatB = this.floatService.save(floatB);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.editModelAndView(floatB, "float.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@ModelAttribute("float") domain.Float floatB,
			final BindingResult binding) {
		ModelAndView result;

		try {
			floatB = this.floatService.reconstruct(floatB, binding);
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(floatB);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
			} else {
				floatB = this.floatService.save(floatB);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(floatB, "float.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final domain.Float floatB,
			final BindingResult binding) {
		ModelAndView result;
		try {
			this.floatService.delete(floatB);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Exception oops) {
			result = this.createEditModelAndView(floatB, "float.commit.error");
		}
		return result;
	}

	private ModelAndView editModelAndView(final Float floatB) {
		ModelAndView result;

		result = this.editModelAndView(floatB, null);
		return result;
	}

	private ModelAndView editModelAndView(final Float floatB,
			final String messageCode) {
		ModelAndView result;
		Brotherhood brotherhood;
		brotherhood = this.brotherhoodService.findByPrincipal();
		
		result = new ModelAndView("float/edit");
		result.addObject("float", floatB);
		result.addObject("parades", this.paradeService.findAllParadesOfOneBrotherhood(brotherhood.getId()));
		result.addObject("message", messageCode);
		return result;
	}

	protected ModelAndView createEditModelAndView(final domain.Float floatB) {
		return this.createEditModelAndView(floatB, null);
	}

	protected ModelAndView createEditModelAndView(final domain.Float floatB,
			final String messageCode) {
		ModelAndView result;
		Brotherhood brotherhood;
		brotherhood = this.brotherhoodService.findByPrincipal();
		
		result = new ModelAndView("float/create");
		result.addObject("float", floatB);
		result.addObject("parades", this.paradeService.findAllParadesOfOneBrotherhood(brotherhood.getId()));
		result.addObject("message", messageCode);

		return result;
	}

}
