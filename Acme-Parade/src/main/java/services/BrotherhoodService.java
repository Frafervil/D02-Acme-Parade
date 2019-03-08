
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Administrator;
import domain.Brotherhood;
import domain.Enrolment;
import forms.BrotherhoodForm;

@Service
@Transactional
public class BrotherhoodService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	@Autowired
	private UserAccountRepository	useraccountRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private Validator				validator;


	// Simple CRUD Methods

	public boolean exists(final Integer arg0) {
		return this.brotherhoodRepository.exists(arg0);
	}

	public Brotherhood create() {
		Brotherhood result;
		Date moment;
		UserAccount userAccount;
		Authority authority;

		result = new Brotherhood();
		userAccount = new UserAccount();
		authority = new Authority();

		moment = new Date(System.currentTimeMillis() - 1);
		authority.setAuthority("BROTHERHOOD");
		userAccount.addAuthority(authority);

		Assert.notNull(moment);
		Assert.notNull(userAccount);

		result.setUserAccount(userAccount);
		result.setEstablishmentDate(moment);
		return result;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		final Brotherhood result, saved;
		UserAccount logedUserAccount;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService.createUserAccount(Authority.BROTHERHOOD);
		Assert.notNull(brotherhood, "brotherhood.not.null");

		if (this.exists(brotherhood.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "brotherhood.notLogged");
			Assert.isTrue(logedUserAccount.equals(brotherhood.getUserAccount()), "brotherhood.notEqual.userAccount");
			saved = this.brotherhoodRepository.findOne(brotherhood.getId());
			Assert.notNull(saved, "brotherhood.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(brotherhood.getUserAccount().getUsername()), "brotherhood.notEqual.username");
			Assert.isTrue(saved.getUserAccount().getPassword().equals(brotherhood.getUserAccount().getPassword()), "brotherhood.notEqual.password");
		} else
			brotherhood.getUserAccount().setPassword(encoder.encodePassword(brotherhood.getUserAccount().getPassword(), null));
		result = this.brotherhoodRepository.save(brotherhood);

		return result;
	}

	public Brotherhood findOne(final int brotherhoodId) {
		Brotherhood result;

		result = this.brotherhoodRepository.findOne(brotherhoodId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Brotherhood> findAll() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public Brotherhood findByPrincipal() {
		Brotherhood result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.brotherhoodRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;

	}

	public BrotherhoodForm construct(final Brotherhood brotherhood) {
		final BrotherhoodForm brotherhoodForm = new BrotherhoodForm();
		brotherhoodForm.setAddress(brotherhood.getAddress());
		brotherhoodForm.setEmail(brotherhood.getEmail());
		brotherhoodForm.setId(brotherhood.getId());
		brotherhoodForm.setMiddleName(brotherhood.getMiddleName());
		brotherhoodForm.setName(brotherhood.getName());
		brotherhoodForm.setPhone(brotherhood.getPhone());
		brotherhoodForm.setPhoto(brotherhood.getPhoto());
		brotherhoodForm.setPictures(brotherhood.getPictures());
		brotherhoodForm.setSurname(brotherhood.getSurname());
		brotherhoodForm.setTitle(brotherhood.getTitle());
		brotherhoodForm.setCheckBox(brotherhoodForm.getCheckBox());
		brotherhoodForm.setArea(brotherhood.getArea());
		brotherhoodForm.setUsername(brotherhood.getUserAccount().getUsername());
		//En los construct no coger la contraseña
		return brotherhoodForm;

	}

	public Brotherhood findByUserAccountId(final int userAccountId) {
		Assert.notNull(userAccountId);
		Brotherhood result;
		result = this.brotherhoodRepository.findByUserAccountId(userAccountId);
		return result;
	}

	public Collection<Brotherhood> findAllBrotherhoodsOfOneMember(final int memberId) {
		Assert.notNull(memberId);
		Collection<Brotherhood> result;
		result = this.brotherhoodRepository.findAllBrotherhoodsOfOneMember(memberId);
		return result;
	}

	public Brotherhood reconstruct(final BrotherhoodForm brotherhoodForm, final BindingResult binding) {
		Brotherhood result;

		result = this.create();
		result.getUserAccount().setUsername(brotherhoodForm.getUsername());
		result.getUserAccount().setPassword(brotherhoodForm.getPassword());
		result.setAddress(brotherhoodForm.getAddress());
		result.setEmail(brotherhoodForm.getEmail());
		result.setMiddleName(brotherhoodForm.getMiddleName());
		result.setName(brotherhoodForm.getName());
		result.setPhone(brotherhoodForm.getPhone());
		result.setPhoto(brotherhoodForm.getPhoto());
		result.setPictures(brotherhoodForm.getPictures());
		result.setSurname(brotherhoodForm.getSurname());
		result.setTitle(brotherhoodForm.getTitle());
		result.setArea(brotherhoodForm.getArea());

		if (!brotherhoodForm.getPassword().equals(brotherhoodForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker", "brotherhood.validation.passwordsNotMatch", "Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(brotherhoodForm.getUsername()).isEmpty())
			binding.rejectValue("username", "brotherhood.validation.usernameExists", "This username already exists");
		if (brotherhoodForm.getCheckBox() == false)
			binding.rejectValue("checkBox", "brotherhood.validation.checkBox", "This checkbox must be checked");

		this.validator.validate(result, binding);
		this.brotherhoodRepository.flush();

		return result;

	}

	public Brotherhood reconstructPruned(final Brotherhood brotherhood, final BindingResult binding) {
		Brotherhood result;

		if (brotherhood.getId() == 0)
			result = brotherhood;
		else
			result = this.brotherhoodRepository.findOne(brotherhood.getId());
		result.setAddress(brotherhood.getAddress());
		result.setEmail(brotherhood.getEmail());
		result.setMessageBoxes(brotherhood.getMessageBoxes());
		result.setMiddleName(brotherhood.getMiddleName());
		result.setName(brotherhood.getName());
		result.setPhone(brotherhood.getPhone());
		result.setPhoto(brotherhood.getPhoto());
		result.setSurname(brotherhood.getSurname());
		result.setPictures(brotherhood.getPictures());
		this.validator.validate(result, binding);
		this.brotherhoodRepository.flush();
		return result;
	}
	public Brotherhood largestBrotherhood() {
		Brotherhood result = null;
		Administrator principal;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int i = 1;

		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		brotherhoods = this.findAll();
		Assert.notNull(brotherhoods);
		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService.findAllActiveEnrolmentsByBrotherhoodId(b.getId());
			if (i == 1)
				result = b;
			if (this.enrolmentService.findByBrotherhoodId(result.getId()).size() < enrolments.size())
				result = b;
			i++;
		}
		return result;
	}
	public Brotherhood smallestBrotherhood() {
		Brotherhood result = null;
		Administrator principal;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int i = 1;

		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		brotherhoods = this.findAll();
		Assert.notNull(brotherhoods);
		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService.findAllActiveEnrolmentsByBrotherhoodId(b.getId());
			if (i == 1)
				result = b;
			if (this.enrolmentService.findByBrotherhoodId(result.getId()).size() > enrolments.size())
				result = b;
			i++;
		}
		return result;
	}

	public Brotherhood findByAreaId(final int areaId) {
		Brotherhood result;

		result = this.brotherhoodRepository.findByAreaId(areaId);
		return result;

	}

}
