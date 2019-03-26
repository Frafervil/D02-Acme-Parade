
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageService {

	// Managed Repository

	@Autowired
	private MessageRepository	messageRepository;

	// Supporting services

	@Autowired
	private MessageBoxService	messageBoxService;

	@Autowired
	private ActorService		actorService;


	//	@Autowired
	//	private CustomisationService	customisationService;

	// Constructor

	public MessageService() {
		super();
	}

	// Simple CRUD methods

	public Message create() {
		Message result;
		Actor principal;
		MessageBox box;
		List<MessageBox> boxes;
		Collection<Actor> recipients;
		Collection<String> tags;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Message();
		result.setSender(principal);
		boxes = new ArrayList<MessageBox>();
		box = this.messageBoxService.findOutBoxActor(principal);
		boxes.add(box);
		result.setMessageBoxes(boxes);
		recipients = new ArrayList<Actor>();
		result.setRecipients(recipients);
		result.setMoment(new Date(System.currentTimeMillis() - 1));
		tags = new ArrayList<String>();
		result.setTags(tags);

		return result;
	}

	// An actor who is authenticated must be able to exchange messages with
	// other actors

	public Message save(final Message message) {
		Message result;
		Message saved;
		Actor principal;
		Date moment;
		boolean isSpam;
		//		Collection<String> spamWords;
		List<MessageBox> boxes;
		Collection<Actor> recipients;
		MessageBox box1;
		Collection<Message> messages;
		MessageBox outBox;

		Assert.notNull(message);

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);

		moment = new Date(System.currentTimeMillis() - 1);
		isSpam = false;

		//		spamWords = this.customisationService.find().getSpamWords();
		//		for (final String spam : spamWords)
		//			if (message.getSubject().toLowerCase().contains(spam.toLowerCase())) {
		//				isSpam = true;
		//				break;
		//			} else if (message.getBody().toLowerCase().contains(spam.toLowerCase())) {
		//				isSpam = true;
		//				break;
		//			}

		message.setMoment(moment);
		message.setIsSpam(isSpam);
		message.setSender(principal);

		//		Assert.isTrue(!message.getRecipients().contains(principal));

		recipients = message.getRecipients();
		boxes = message.getMessageBoxes();
		Assert.notNull(boxes);
		outBox = message.getMessageBoxes().iterator().next();

		saved = this.messageRepository.save(message);
		Assert.notNull(saved);

		if (isSpam) {
			//En caso de ser spam, message se asocia a todas las spamBoxes de los recipients a los que llega
			for (final Actor a : recipients) {
				box1 = this.messageBoxService.findSpamBoxActor(a);
				boxes.add(box1);
				messages = box1.getMessages();
				messages.add(saved);
				box1.setMessages(messages);
				this.messageBoxService.save2(box1, a);
			}
			//			principal.setIsSuspicious(true);
			this.actorService.save(principal);

		} else
			// De no ser así, llega a las inBoxes de los recipients
			for (final Actor a : recipients) {
				box1 = this.messageBoxService.findInBoxActor(a);
				boxes.add(box1);
				messages = box1.getMessages();
				messages.add(saved);
				box1.setMessages(messages);
				this.messageBoxService.save2(box1, a);
			}

		saved.setMessageBoxes(boxes);

		result = this.messageRepository.save(saved);
		Assert.notNull(result);

		Collection<Message> ms;
		ms = outBox.getMessages();
		ms.add(result);
		outBox.setMessages(ms);
		this.messageBoxService.save2(outBox, principal);

		return result;
	}

	public void delete(final Message message) {
		Actor principal;
		MessageBox trashBox;
		Assert.notNull(message);

		Assert.isTrue(message.getId() != 0);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		trashBox = this.messageBoxService.findTrashBoxActor(principal);
		if (trashBox.getMessages().contains(message)) {
			final Collection<Message> messagesTrash = trashBox.getMessages();
			messagesTrash.remove(message);
			trashBox.setMessages(messagesTrash);
			this.messageBoxService.save2(trashBox, principal);
			message.getMessageBoxes().remove(trashBox);
			if (message.getMessageBoxes().isEmpty())
				this.messageRepository.delete(message);
		} else
			this.move(message, trashBox);
	}

	public Message findOne(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	// Other business methods

	public void move(final Message message, final MessageBox destination) {

		Actor principal;
		MessageBox origin = null;
		Collection<Message> updatedOriginBox;
		Collection<Message> updatedDestinationBox;
		Message m;
		List<MessageBox> boxes;
		List<MessageBox> boxes2;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		//¿En qué buzón de correo está el mensaje que queremos mover?
		boxes = principal.getMessageBoxes();
		for (final MessageBox box : boxes)
			if (box.getMessages().contains(message)) {
				origin = box;
				break;
			}

		Assert.notNull(message);
		Assert.notNull(origin);
		Assert.notNull(destination);

		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(origin.getId() != 0);
		Assert.isTrue(destination.getId() != 0);

		Assert.isTrue(origin.getMessages().contains(message) || destination.getMessages().contains(message));
		Assert.isTrue(principal.getMessageBoxes().contains(origin));
		Assert.isTrue(principal.getMessageBoxes().contains(destination));

		boxes2 = message.getMessageBoxes();
		boxes2.add(destination);
		boxes2.remove(origin);
		message.setMessageBoxes(boxes2);

		m = this.messageRepository.save(message);

		updatedOriginBox = origin.getMessages();
		updatedDestinationBox = destination.getMessages();

		updatedOriginBox.remove(m);
		updatedDestinationBox.add(m);

		origin.setMessages(updatedOriginBox);
		destination.setMessages(updatedDestinationBox);

		this.messageBoxService.save2(origin, principal);
		this.messageBoxService.save2(destination, principal);
	}

	public Message broadcast(final Message message) {
		Message result;
		Message saved;
		Actor principal;
		Date moment;
		boolean isSpam;
		//		Collection<String> spamWords;
		List<MessageBox> boxes;
		Collection<Actor> recipients;
		MessageBox box1;
		Collection<Message> messages;
		MessageBox outBox;

		Assert.notNull(message);

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);

		moment = new Date(System.currentTimeMillis() - 1);
		isSpam = false;

		//		spamWords = this.customisationService.find().getSpamWords();
		//		for (final String spam : spamWords)
		//			if (message.getSubject().toLowerCase().contains(spam.toLowerCase())) {
		//				isSpam = true;
		//				break;
		//			} else if (message.getBody().toLowerCase().contains(spam.toLowerCase())) {
		//				isSpam = true;
		//				break;
		//			}

		message.setMoment(moment);
		message.setIsSpam(isSpam);
		message.setSender(principal);

		Assert.isTrue(!message.getRecipients().contains(principal));

		recipients = this.actorService.findAll();
		boxes = message.getMessageBoxes();
		Assert.notNull(boxes);
		outBox = message.getMessageBoxes().iterator().next();

		saved = this.messageRepository.save(message);
		Assert.notNull(saved);

		if (isSpam) {
			//En caso de ser spam, message se asocia a todas las spamBoxes de los recipients a los que llega
			for (final Actor a : recipients) {
				box1 = this.messageBoxService.findSpamBoxActor(a);
				boxes.add(box1);
				messages = box1.getMessages();
				messages.add(saved);
				box1.setMessages(messages);
				this.messageBoxService.save2(box1, a);
			}
			//			principal.setIsSuspicious(true);
			this.actorService.save(principal);

		} else
			// De no ser así, llega a las inBoxes de los recipients
			for (final Actor a : recipients) {
				box1 = this.messageBoxService.findNotificationBoxActor(a);
				boxes.add(box1);
				messages = box1.getMessages();
				messages.add(saved);
				box1.setMessages(messages);
				this.messageBoxService.save2(box1, a);
			}

		saved.setMessageBoxes(boxes);

		result = this.messageRepository.save(saved);
		Assert.notNull(result);

		Collection<Message> ms;
		ms = outBox.getMessages();
		ms.add(result);
		outBox.setMessages(ms);
		this.messageBoxService.save2(outBox, principal);

		return result;
	}

	public void flush() {
		this.messageRepository.flush();
	}

	public Message createAndSaveNotificationStatusApplicationChanged(final Actor actor, final String body, final Date moment) {
		Message result;
		final List<MessageBox> mb = new ArrayList<MessageBox>();
		final Collection<Actor> actors = new ArrayList<Actor>();
		actors.add(actor);
		final MessageBox ib = this.messageBoxService.findInBoxActor(actor);
		mb.add(ib);
		final ArrayList<String> tag = new ArrayList<String>();
		tag.add("Notificacion");
		result = new Message();
		result.setSender(actor);
		result.setRecipients(actors);
		result.setPriority("NEUTRAL");
		result.setMoment(moment);
		result.setIsSpam(false);
		result.setMessageBoxes(mb);
		result.setBody(body);
		result.setTags(tag);
		result.setSubject("New status update");

		final Collection<Message> re = new ArrayList<Message>();
		for (final Message e : result.getMessageBoxes().get(0).getMessages())
			re.add(e);
		re.add(result);
		result.getMessageBoxes().get(0).setMessages(re);

		this.messageRepository.save(result);

		return result;
	}

}
