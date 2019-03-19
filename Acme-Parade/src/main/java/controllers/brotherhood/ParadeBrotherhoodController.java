
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

import services.ActorService;
import services.BrotherhoodService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Actor;
import domain.Brotherhood;
import domain.Parade;

@Controller
@RequestMapping("/parade/brotherhood")
public class ParadeBrotherhoodController extends AbstractController {

	@Autowired
	private ParadeService	paradeService;
	
	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ActorService	actorService;
	

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		Brotherhood brotherhood;
		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		Collection<Parade> parades;
		//Comprobar que si el principal coincide con el brotherhoodId que se le pasa al list, hacer el findVisibleProcessions
		//En caso contrario, hacer el método findAllProcessionsOfOneBrotherhood
		final Actor principal = this.actorService.findByPrincipal();
		if (principal.getUserAccount().getUsername().equals(brotherhood.getUserAccount().getUsername()))
			parades = this.paradeService.findVisibleParades(brotherhood);
		else
			parades = this.paradeService.findAllFinalOfOneBrotherhood(brotherhoodId);

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("requestURI", "parade/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/listAnonymous", method = RequestMethod.GET)
	public ModelAndView listAnonymous(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Parade> parades;
		parades = this.paradeService.findAllFinalOfOneBrotherhood(brotherhoodId);

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("requestURI", "parade/brotherhood/list.do");
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.create();
		result = this.createModelAndView(parade);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);
		result = this.createEditModelAndView(parade);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("parade") Parade parade, final BindingResult binding) {
		ModelAndView result;

		try {
			parade = this.paradeService.reconstruct(parade, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(parade);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				parade = this.paradeService.save(parade);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(parade, "parade.commit.error");
		}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView saveDraft(@ModelAttribute("parade") Parade parade, final BindingResult binding) {
		ModelAndView result;

		try {
			parade = this.paradeService.reconstruct(parade, binding);
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(parade);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				parade = this.paradeService.saveAsDraft(parade);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(@ModelAttribute("parade") Parade parade, final BindingResult binding) {
		ModelAndView result;

		try {
			parade = this.paradeService.reconstruct(parade, binding);
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				result = this.createEditModelAndView(parade);
			} else {
				this.paradeService.save(parade);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Parade parade, final BindingResult binding) {
		ModelAndView result;
		try {
			this.paradeService.delete(parade);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
		}
		return result;
	}

	// -------------------
	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createEditModelAndView(parade, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("parade/edit");
		result.addObject("parade", parade);

		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView createModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createModelAndView(parade, null);
		return result;
	}

	private ModelAndView createModelAndView(final Parade parade, final String messageCode) {
		ModelAndView result;
		result = new ModelAndView("parade/create");
		result.addObject("parade", parade);
		result.addObject("message", messageCode);
		return result;
	}

}
