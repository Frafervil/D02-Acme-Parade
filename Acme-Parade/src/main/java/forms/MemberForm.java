
package forms;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

public class MemberForm {

	// --- AdministratorAtributes ---------------------------------------------
	private int	idMember;


	public int getIdMember() {
		return this.idMember;
	}

	public void setIdMember(final int idMember) {
		this.idMember = idMember;
	}


	private String	name;
	private String	middleName;
	private String	surname;
	private String	photo;
	private String	email;
	private String	phone;
	private String	address;


	// --- Getters y Setters ---------------------------------------

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9 ]*[<]?\\w+[@][a-zA-Z0-9.]+[>]?$")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}


	// --- UserAccountAtributes ---------------------------------------

	private String	username;
	private String	password;


	// --- Getters y Setters ---------------------------------------

	@NotBlank
	@Size(min = 5, max = 32)
	@Column(unique = true)
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@NotBlank
	@Size(min = 5, max = 32)
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}


	// --- Others -------------------------------------------------

	private String	passwordChecker;
	private boolean	checkBox;


	@NotBlank
	@Size(min = 5, max = 32)
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPasswordChecker() {
		return this.passwordChecker;
	}

	public boolean getCheckBox() {
		return this.checkBox;
	}

	public void setCheckBox(final boolean checkBox) {
		this.checkBox = checkBox;
	}

	public void setPasswordChecker(final String passwordChecker) {
		this.passwordChecker = passwordChecker;
	}

}
