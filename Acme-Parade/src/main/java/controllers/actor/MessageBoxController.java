
package controllers.actor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageBoxService;
import controllers.AbstractController;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Controller
@RequestMapping("/messageBox/actor")
public class MessageBoxController extends AbstractController {

	// Services

	@Autowired
	private MessageBoxService	messageBoxService;

	@Autowired
	private ActorService		actorService;


	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<MessageBox> boxes;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		boxes = principal.getMessageBoxes();

		result = new ModelAndView("messageBox/list");
		result.addObject("messageBoxes", boxes);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = {
		"messageBoxId"
	})
	public ModelAndView list(@RequestParam final int messageBoxId) {
		final ModelAndView result;
		MessageBox currentBox;
		Collection<Message> messages;

		currentBox = this.messageBoxService.findOne(messageBoxId);
		messages = currentBox.getMessages();

		result = new ModelAndView("messageBox/list");
		result.addObject("currentMessageBox", currentBox);
		result.addObject("messages", messages);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		MessageBox box;

		box = this.messageBoxService.create();

		result = this.createEditModelAndView(box);

		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageBoxId) {
		ModelAndView result;
		MessageBox box;

		box = this.messageBoxService.findOne(messageBoxId);
		Assert.notNull(box);
		result = this.createEditModelAndView(box);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MessageBox box, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(box);
		else
			try {
				this.messageBoxService.save(box);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(box, "messageBox.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MessageBox box, final BindingResult binding) {
		ModelAndView result;

		try {
			this.messageBoxService.delete(box);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(box, "messageBox.commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final MessageBox box) {
		ModelAndView result;

		result = this.createEditModelAndView(box, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageBox box, final String messageCode) {
		ModelAndView result;
		Collection<Message> messages;
		String name;
		boolean permission = false;
		Collection<MessageBox> boxes;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		if (box.getId() == 0)
			permission = true;
		else
			for (final MessageBox b : principal.getMessageBoxes())
				if (b.getId() == box.getId()) {
					permission = true;
					break;
				}

		messages = box.getMessages();

		if (box.getName() == null)
			name = null;
		else
			name = box.getName();

		boxes = principal.getMessageBoxes();
		boxes.remove(box);

		result = new ModelAndView("messageBox/edit");
		result.addObject("messageBox", box);
		result.addObject("name", name);
		result.addObject("messages", messages);
		result.addObject("parentBoxes", boxes);
		result.addObject("permission", permission);

		result.addObject("message", messageCode);

		return result;
	}
}
