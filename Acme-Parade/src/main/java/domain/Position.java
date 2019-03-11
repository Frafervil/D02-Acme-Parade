
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Position extends DomainEntity {

	private String	spanishName;
	private String	englishName;


	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getSpanishName() {
		return this.spanishName;
	}
	public void setSpanishName(final String spanishName) {
		this.spanishName = spanishName;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getEnglishName() {
		return this.englishName;
	}
	public void setEnglishName(final String englishName) {
		this.englishName = englishName;
	}

}
