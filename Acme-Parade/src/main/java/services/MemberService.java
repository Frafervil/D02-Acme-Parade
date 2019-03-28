
package services;

import java.util.ArrayList;
import java.util.Collection;
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

import repositories.CustomisationRepository;
import repositories.MemberRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Administrator;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Request;
import forms.MemberForm;

@Service
@Transactional
public class MemberService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private MemberRepository		memberRepository;

	@Autowired
	private UserAccountRepository	useraccountRepository;

	@Autowired
	private CustomisationRepository	customisationRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageBoxService		messageBoxService;

	@Autowired
	private Validator				validator;


	// Additional functions

	// Simple CRUD Methods

	public Member create() {
		Member result;

		result = new Member();

		//Nuevo userAccount con Member en la lista de authorities
		final UserAccount userAccount = this.actorService.createUserAccount(Authority.MEMBER);

		result.setUserAccount(userAccount);

		return result;
	}

	public Member save(final Member member) {
		Member result, saved;
		UserAccount logedUserAccount;

		final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService.createUserAccount(Authority.MEMBER);
		Assert.notNull(member, "member.not.null");

		if (member.getId() == 0)
			member.getUserAccount().setPassword(passwordEncoder.encodePassword(member.getUserAccount().getPassword(), null));
		else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "member.notLogged");
			Assert.isTrue(logedUserAccount.equals(member.getUserAccount()), "memer.notEqual.userAccount");
			saved = this.memberRepository.findOne(member.getId());
			Assert.notNull(saved, "member.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(member.getUserAccount().getUsername()));
			Assert.isTrue(saved.getUserAccount().getPassword().equals(member.getUserAccount().getPassword()));

			//			final Collection<Message> messages = new LinkedList<>();
			//			final MessageBox inbox = new MessageBox();
			//			inbox.setName("InBox");
			//			inbox.setIsSystemBox(true);
			//			inbox.setMessages(messages);
			//			final MessageBox outbox = new MessageBox();
			//			outbox.setName("OutBox");
			//			outbox.setIsSystemBox(true);
			//			outbox.setMessages(messages);
			//			final MessageBox trashbox = new MessageBox();
			//			trashbox.setName("TrashBox");
			//			trashbox.setIsSystemBox(true);
			//			trashbox.setMessages(messages);
			//			final MessageBox spambox = new MessageBox();
			//			spambox.setName("SpamBox");
			//			spambox.setIsSystemBox(true);
			//			spambox.setMessages(messages);
			//			final MessageBox notificationbox = new MessageBox();
			//			notificationbox.setName("NotificationBox");
			//			notificationbox.setIsSystemBox(true);
			//			notificationbox.setMessages(messages);
			//			final List<MessageBox> boxes = new ArrayList<MessageBox>();
			//			boxes.add(this.messageBoxService.save(inbox));
			//			boxes.add(this.messageBoxService.save(outbox));
			//			boxes.add(this.messageBoxService.save(trashbox));
			//			boxes.add(this.messageBoxService.save(spambox));
			//			boxes.add(this.messageBoxService.save(notificationbox));
			//			member.setMessageBoxes(boxes);
		}

		result = this.memberRepository.save(member);
		return result;
	}

	public void delete() {
		Member principal;
		final Collection<Enrolment> enrolments;
		final Collection<Request> requests;

		principal = this.findByPrincipal();
		Assert.notNull(principal);

		enrolments = this.enrolmentService.findByBrotherhoodId(principal.getId());
		for (final Enrolment e : enrolments)
			this.enrolmentService.deleteEnroll(e);

		requests = this.requestService.findAllByMember(principal.getId());
		for (final Request r : requests)
			this.requestService.deleteRequestDeletingProfile(r);

		this.memberRepository.delete(principal);
	}

	public Member findOne(final int memberId) {
		Member result;

		result = this.memberRepository.findOne(memberId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Member> findAll() {
		Collection<Member> result;

		result = this.memberRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Member findByPrincipal() {
		Member result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.memberRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;

	}

	public Member findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Member result;
		result = this.memberRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.memberRepository.exists(arg0);
	}

	public MemberForm construct(final Member member) {
		final MemberForm memberForm = new MemberForm();
		memberForm.setAddress(member.getAddress());
		memberForm.setEmail(member.getEmail());
		memberForm.setIdMember(member.getId());
		memberForm.setMiddleName(member.getMiddleName());
		memberForm.setName(member.getName());
		memberForm.setPhone(member.getPhone());
		memberForm.setPhoto(member.getPhoto());
		memberForm.setSurname(member.getSurname());
		memberForm.setCheckBox(memberForm.getCheckBox());
		memberForm.setUsername(member.getUserAccount().getUsername());
		return memberForm;
	}

	public Member reconstruct(final MemberForm memberForm, final BindingResult binding) {
		Member result;

		result = this.create();
		result.getUserAccount().setUsername(memberForm.getUsername());
		result.getUserAccount().setPassword(memberForm.getPassword());
		result.setAddress(memberForm.getAddress());
		result.setEmail(memberForm.getEmail());
		result.setMiddleName(memberForm.getMiddleName());
		result.setName(memberForm.getName());
		result.setPhoto(memberForm.getPhoto());
		result.setSurname(memberForm.getSurname());

		if (!StringUtils.isEmpty(memberForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(memberForm.getPhone());
			if (matcher.matches())
				memberForm.setPhone(this.customisationRepository.findAll().iterator().next().getCountryCode() + memberForm.getPhone());
		}
		result.setPhone(memberForm.getPhone());

		if (!memberForm.getPassword().equals(memberForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker", "member.validation.passwordsNotMatch", "Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(memberForm.getUsername()).isEmpty())
			binding.rejectValue("username", "member.validation.usernameExists", "This username already exists");
		if (memberForm.getCheckBox() == false)
			binding.rejectValue("checkBox", "member.validation.checkBox", "This checkbox must be checked");

		this.validator.validate(result, binding);
		this.memberRepository.flush();

		return result;
	}

	public Member reconstructPruned(final Member member, final BindingResult binding) {
		Member result;

		if (member.getId() == 0)
			result = member;
		else
			result = this.memberRepository.findOne(member.getId());
		result.setAddress(member.getAddress());
		result.setEmail(member.getEmail());
		result.setMessageBoxes(member.getMessageBoxes());
		result.setMiddleName(member.getMiddleName());
		result.setName(member.getName());
		result.setPhoto(member.getPhoto());
		result.setSurname(member.getSurname());

		if (!StringUtils.isEmpty(member.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(member.getPhone());
			if (matcher.matches())
				member.setPhone(this.customisationRepository.findAll().iterator().next().getCountryCode() + member.getPhone());
		}
		result.setPhone(member.getPhone());

		this.validator.validate(result, binding);
		this.memberRepository.flush();
		return result;
	}

	// Business Method
	public Collection<Member> findAllMembersOfOneBrotherhood(final int brotherhoodId) {
		Collection<Member> result;

		result = this.memberRepository.findAllMembersOfOneBrotherhood(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Member> findAllActiveMembersOfOneBrotherhood(final int brotherhoodId) {
		Collection<Member> result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		Assert.notNull(brotherhood);

		result = this.memberRepository.findAllActiveMembersOfOneBrotherhood(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	// Dashboard

	public Double averageMemberPerBrotherhood() {
		Administrator principal;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int total = 0;
		final Double result;

		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		brotherhoods = this.brotherhoodService.findAll();

		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService.findAllActiveEnrolmentsByBrotherhoodId(b.getId());
			total = total + enrolments.size();
		}
		if (brotherhoods.size() == 0)
			result = 0.0;
		else
			result = (double) (total / (brotherhoods.size()));

		return result;
	}
	public Double minMemberPerBrotherhood() {
		Administrator principal;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int i = 1;
		Double result = 0.0;

		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		brotherhoods = this.brotherhoodService.findAll();
		Assert.notNull(brotherhoods);
		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService.findAllActiveEnrolmentsByBrotherhoodId(b.getId());
			if (i == 1)
				result = (double) enrolments.size();
			if (enrolments.size() < result)
				result = (double) enrolments.size();
			i++;
		}
		return result;
	}

	public Double maxMemberPerBrotherhood() {
		Administrator principal;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int i = 1;
		Double result = 0.0;

		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		brotherhoods = this.brotherhoodService.findAll();

		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService.findAllActiveEnrolmentsByBrotherhoodId(b.getId());
			if (i == 1)
				result = (double) enrolments.size();
			if (result < enrolments.size())
				result = (double) enrolments.size();
			i++;
		}
		return result;
	}

	public Double stddevMemberPerBrotherhood() {
		Administrator principal;
		Collection<Brotherhood> brotherhoods;
		Collection<Enrolment> enrolments;
		int total = 0;
		final Double result;

		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		brotherhoods = this.brotherhoodService.findAll();
		for (final Brotherhood b : brotherhoods) {
			enrolments = this.enrolmentService.findAllActiveEnrolmentsByBrotherhoodId(b.getId());
			total = total + enrolments.size();
		}

		if (brotherhoods.size() == 0)
			result = 0.0;
		else
			result = (double) (total / (brotherhoods.size()));

		return result;
	}

	public Collection<Member> mostapprovedMembers() {
		final Collection<Member> members;
		final Collection<Member> result = new ArrayList<Member>();
		Collection<Request> requests;
		int i = 0;
		double p = 0.0;

		members = this.findAll();
		for (final Member m : members) {
			requests = this.requestService.findAllByMember(m.getId());
			i = 0;
			p = 0.0;
			for (final Request r : requests) {
				i++;
				if (r.getStatus().equals("APPROVED"))
					p = p + 1;
			}
			if (p / i > 0.1)
				result.add(m);
		}

		return result;
	}

	public void flush() {
		this.memberRepository.flush();
	}

}
