
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageBoxRepository;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageBoxService {

	// Managed Repository

	@Autowired
	private MessageBoxRepository	messageBoxRepository;

	// Supporting services

	@Autowired
	private ActorService			actorService;


	// Constructors

	public MessageBoxService() {
		super();
	}

	// Simple CRUD methods

	public MessageBox create() {
		MessageBox result;
		Actor principal;
		Collection<Message> messages;

		messages = new ArrayList<Message>();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new MessageBox();
		Assert.notNull(result);

		result.setActor(principal);
		result.setMessages(messages);

		return result;
	}

	public MessageBox save(final MessageBox box) {
		MessageBox result;
		Actor principal;
		Collection<Message> messages;
		List<MessageBox> boxes;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = this.messageBoxRepository.save(box);
		Assert.notNull(result);

		if (box.getId() == 0) {

			box.setIsSystemBox(false);
			messages = new ArrayList<Message>();
			box.setMessages(messages);

		} else {

			Assert.isTrue(!box.getIsSystemBox());
			Assert.isTrue(principal.getMessageBoxes().contains(box));

		}

		if (!principal.getMessageBoxes().contains(result)) {
			boxes = principal.getMessageBoxes();
			boxes.add(result);
			principal.setMessageBoxes(boxes);
			this.actorService.save(principal);
		}

		return result;
	}

	public void save2(final MessageBox box, final Actor actor) {
		MessageBox result;

		Assert.notNull(actor);
		Assert.isTrue(actor.getMessageBoxes().contains(box));

		result = this.messageBoxRepository.save(box);
		Assert.notNull(result);
	}

	public void delete(final MessageBox box) {
		Actor principal;

		Assert.isTrue(box.getId() != 0);
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(!box.getIsSystemBox());
		Assert.isTrue(principal.getMessageBoxes().contains(box));

		this.messageBoxRepository.delete(box);
		principal.getMessageBoxes().remove(box);
	}

	public List<MessageBox> createSystemBoxes(final Actor actor) {

		List<MessageBox> result;
		Collection<String> names;
		Collection<Message> messages;
		MessageBox saved;

		names = new ArrayList<String>();
		names.add("in box");
		names.add("out box");
		names.add("spam box");
		names.add("trash box");
		names.add("notification box");

		result = new ArrayList<MessageBox>();
		for (final String name : names) {
			final MessageBox box = new MessageBox();
			box.setName(name);
			box.setIsSystemBox(true);
			messages = new ArrayList<Message>();
			box.setMessages(messages);
			box.setActor(actor);
			saved = this.messageBoxRepository.save(box);
			Assert.notNull(saved);

			result.add(saved);
		}

		return result;
	}

	public MessageBox findOne(final int boxId) {
		MessageBox result;
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		result = this.messageBoxRepository.findOne(boxId);
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public MessageBox findInBoxActor(final Actor a) {
		Actor principal;
		MessageBox result;

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.messageBoxRepository.findInBoxMessageBoxByActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public MessageBox findOutBoxActor(final Actor a) {
		Actor principal;
		MessageBox result;

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.messageBoxRepository.findOutBoxMessageBoxByActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public MessageBox findSpamBoxActor(final Actor a) {
		Actor principal;
		MessageBox result;
		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.messageBoxRepository.findSpamMessageBoxByActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public MessageBox findTrashBoxActor(final Actor a) {
		Actor principal;
		MessageBox result;
		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.messageBoxRepository.findTrashMessageBoxByActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public MessageBox findNotificationBoxActor(final Actor a) {
		Actor principal;
		MessageBox result;
		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.messageBoxRepository.findNotificationMessageBoxByActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public Collection<MessageBox> findChildBoxes(final int messageBoxId) {
		Collection<MessageBox> result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		result = this.messageBoxRepository.findChildBoxes(messageBoxId);
		Assert.notNull(result);
		return result;
	}

	public void flush() {
		this.messageBoxRepository.flush();
	}

	//	public MessageBox findInBoxMessageBoxActor(final Actor a) {
	//		MessageBox result = null;
	//		final String sas = "in box";
	//		Assert.notNull(a);
	//		Assert.isTrue(a.getId() != 0);
	//		for (final MessageBox m : a.getMessageBoxes())
	//			if (m.getName().equals(sas))
	//				result = m;
	//		Assert.notNull(result);
	//		return result;
	//	}
}
