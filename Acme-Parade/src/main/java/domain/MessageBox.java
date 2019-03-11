
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MessageBox extends DomainEntity {

	private String				name;
	private boolean				isSystemBox;
	private Actor				actor;
	private Collection<Message>	messages;
	private MessageBox			parentBox;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean getIsSystemBox() {
		return this.isSystemBox;
	}

	public void setIsSystemBox(final boolean isSystemBox) {
		this.isSystemBox = isSystemBox;
	}

	// Relationships----------------------------------------------

	@NotNull
	@ManyToMany
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

	@Valid
	@ManyToOne(optional = true)
	public MessageBox getParentBox() {
		return this.parentBox;
	}

	public void setParentBox(final MessageBox parentBox) {
		this.parentBox = parentBox;
	}
}
