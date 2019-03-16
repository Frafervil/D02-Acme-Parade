
package controllers.actor;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageBoxService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Controller
@RequestMapping("/message/actor")
public class MessageController extends AbstractController {

	// Services

	@Autowired
	private MessageService		messageService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MessageBoxService	messageBoxService;


	// Listing

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Message mensaje;

		mensaje = this.messageService.create();

		result = this.createEditModelAndView(mensaje);

		return result;

	}

	// Edition ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {
		ModelAndView result;
		Message message;

		message = this.messageService.findOne(messageId);
		Assert.notNull(message);
		result = this.createEditModelAndView(message);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("mensaje") @Valid final Message mensaje, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(mensaje);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.messageService.save(mensaje);
				result = new ModelAndView("redirect:/messageBox/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(mensaje, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "move")
	public ModelAndView move(@Valid final Message mensaje, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(mensaje);
			System.out.println(binding.getAllErrors());
		} else {

			this.messageService.move(mensaje, mensaje.getMessageBoxes().get(mensaje.getMessageBoxes().size() - 1));
			result = new ModelAndView("redirect:/messageBox/actor/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Message mensaje, final BindingResult binding) {
		ModelAndView result;

		try {
			this.messageService.delete(mensaje);
			result = new ModelAndView("redirect:/messageBox/actor/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(mensaje, "message.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/exportData")
	public ModelAndView exportData() {
		ModelAndView result;
		Message mensaje;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		try {
			mensaje = this.messageService.create();
			mensaje.getRecipients().add(principal);
			mensaje.setBody("Name: " + principal.getName() + " || Middle name: " + principal.getMiddleName() + " || Surname: " + principal.getSurname() + " || Photo: " + principal.getPhoto() + " || Phone: " + principal.getPhone() + " || Address: "
				+ principal.getAddress() + " || User name: " + principal.getUserAccount().getUsername());
			mensaje.setPriority("HIGH");
			mensaje.setSubject("Your data");

			this.messageService.save(mensaje);

			result = new ModelAndView("redirect:/messageBox/actor/list.do?messageBoxId=" + this.messageBoxService.findInBoxActor(principal).getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/messageBox/actor/list.do");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Message mensaje) {
		ModelAndView result;

		result = this.createEditModelAndView(mensaje, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message mensaje, final String messageCode) {
		final ModelAndView result;
		Date moment;
		MessageBox box;
		Actor sender;
		Collection<Actor> recipients;
		final Collection<MessageBox> boxes;
		final boolean permission = true;
		Actor principal;

		moment = mensaje.getMoment();
		box = mensaje.getMessageBoxes().iterator().next();
		sender = mensaje.getSender();
		recipients = this.actorService.findAllMinusPrincipal();
		principal = this.actorService.findByPrincipal();
		boxes = principal.getMessageBoxes();

		result = new ModelAndView("message/edit");
		result.addObject("moment", moment);
		result.addObject("messageBox", box);
		result.addObject("sender", sender);
		result.addObject("mensaje", mensaje);
		result.addObject("recipients", recipients);
		result.addObject("messageBoxes", boxes);
		result.addObject("requestURI", "message/actor/edit.do");
		result.addObject("broadcast", false);
		result.addObject("permission", permission);

		result.addObject("message", messageCode);

		return result;
	}
}
