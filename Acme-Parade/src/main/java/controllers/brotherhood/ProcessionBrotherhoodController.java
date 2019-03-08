
package controllers.brotherhood;

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

import services.ProcessionService;
import controllers.AbstractController;
import domain.Procession;

@Controller
@RequestMapping("/procession/brotherhood")
public class ProcessionBrotherhoodController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		final Collection<Procession> processions = this.processionService.findAllFinalOfOneBrotherhood(brotherhoodId);

		result = new ModelAndView("procession/list");
		result.addObject("processions", processions);
		result.addObject("requestURI", "procession/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Procession procession;

		procession = this.processionService.create();
		result = this.createModelAndView(procession);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession;

		procession = this.processionService.findOne(processionId);
		Assert.notNull(procession);
		result = this.createEditModelAndView(procession);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("procession") Procession procession, final BindingResult binding) {
		ModelAndView result;

		try {
			procession = this.processionService.reconstruct(procession, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(procession);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				procession = this.processionService.save(procession);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(procession, "procession.commit.error");
		}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView saveDraft(@ModelAttribute("procession") Procession procession, final BindingResult binding) {
		ModelAndView result;

		try {
			procession = this.processionService.reconstruct(procession, binding);
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(procession);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				procession = this.processionService.saveAsDraft(procession);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession, "procession.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(@ModelAttribute("procession") Procession procession, final BindingResult binding) {
		ModelAndView result;

		try {
			procession = this.processionService.reconstruct(procession, binding);
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				result = this.createEditModelAndView(procession);
			} else {
				this.processionService.save(procession);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession, "procession.commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Procession procession, final BindingResult binding) {
		ModelAndView result;
		try {
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession, "procession.commit.error");
		}
		return result;
	}

	// -------------------
	protected ModelAndView createEditModelAndView(final Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Procession procession, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("procession/edit");
		result.addObject("procession", procession);

		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView createModelAndView(final Procession procession) {
		ModelAndView result;

		result = this.createModelAndView(procession, null);
		return result;
	}

	private ModelAndView createModelAndView(final Procession procession, final String messageCode) {
		ModelAndView result;
		result = new ModelAndView("procession/create");
		result.addObject("procession", procession);
		result.addObject("message", messageCode);
		return result;
	}

}
