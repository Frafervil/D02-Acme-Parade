
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import repositories.CustomisationRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Administrator;
import domain.Brotherhood;
import domain.Enrolment;
import domain.History;
import domain.MessageBox;
import domain.Parade;
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
	private HistoryService			historyService;

	@Autowired
	private FloatService			floatService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private Validator				validator;

	@Autowired
	private CustomisationRepository	customisationRepository;
	@Autowired
	private MessageBoxService		messageBoxService;


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

		result.setMessageBoxes(new ArrayList<MessageBox>());

		return result;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		Brotherhood saved;
		UserAccount logedUserAccount;
		Md5PasswordEncoder encoder;
		List<MessageBox> messageBoxes;

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

			saved = this.brotherhoodRepository.save(brotherhood);

		} else {
			brotherhood.getUserAccount().setPassword(encoder.encodePassword(brotherhood.getUserAccount().getPassword(), null));
			saved = this.brotherhoodRepository.saveAndFlush(brotherhood);
			messageBoxes = this.messageBoxService.createSystemBoxes(saved);
			saved.setMessageBoxes(messageBoxes);
		}
		return saved;
	}

	public void delete() {
		Brotherhood principal;
		History history;
		Collection<domain.Float> floats;
		final Collection<Parade> parades;
		final Collection<Enrolment> enrolments;

		principal = this.findByPrincipal();
		Assert.notNull(principal);

		history = this.historyService.findByBrotherhoodId(principal.getId());
		if (history != null)
			this.historyService.delete(history);

		floats = this.floatService.findByBrotherhoodId(principal.getId());
		for (final domain.Float f : floats)
			this.floatService.delete(f);

		parades = this.paradeService.findAllParadesOfOneBrotherhood(principal.getId());
		for (final Parade p : parades)
			this.paradeService.delete(p);

		enrolments = this.enrolmentService.findByBrotherhoodId(principal.getId());
		for (final Enrolment e : enrolments)
			this.enrolmentService.deleteEnroll(e);

		this.brotherhoodRepository.delete(principal);
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
		//En los construct no coger la contraseņa
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
		result.setPhoto(brotherhoodForm.getPhoto());
		result.setPictures(brotherhoodForm.getPictures());
		result.setSurname(brotherhoodForm.getSurname());
		result.setTitle(brotherhoodForm.getTitle());
		result.setArea(brotherhoodForm.getArea());

		if (!StringUtils.isEmpty(brotherhoodForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(brotherhoodForm.getPhone());
			if (matcher.matches())
				brotherhoodForm.setPhone(this.customisationRepository.findAll().iterator().next().getCountryCode() + brotherhoodForm.getPhone());
		}
		result.setPhone(brotherhoodForm.getPhone());

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
		result.setPhoto(brotherhood.getPhoto());
		result.setSurname(brotherhood.getSurname());
		result.setPictures(brotherhood.getPictures());
		result.setTitle(brotherhood.getTitle());

		if (!StringUtils.isEmpty(brotherhood.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(brotherhood.getPhone());
			if (matcher.matches())
				brotherhood.setPhone(this.customisationRepository.findAll().iterator().next().getCountryCode() + brotherhood.getPhone());
		}
		result.setPhone(brotherhood.getPhone());

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

	public void flush() {
		this.brotherhoodRepository.flush();
	}

}
