
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Service
@Transactional
public class EnrolmentService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private EnrolmentRepository	enrolmentRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private Validator			validator;


	// Simple CRUD Methods

	public Enrolment create(final Member member) {

		Brotherhood principal;
		Enrolment result;
		Date moment;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);
		result = new Enrolment();
		moment = new Date(System.currentTimeMillis() - 1000);

		result.setBrotherhood(principal);
		result.setMember(member);
		result.setEnrolmentMoment(moment);

		return result;
	}

	public Enrolment save(final Enrolment enrolment) {

		Brotherhood principal;
		Enrolment result;

		Assert.notNull(enrolment);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(enrolment.getBrotherhood().getId() == principal.getId());

		result = this.enrolmentRepository.save(enrolment);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Enrolment enrolment) {

		Brotherhood principal;
		Date moment;

		Assert.notNull(enrolment);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		moment = new Date(System.currentTimeMillis() - 1000);

		enrolment.setDropOutMoment(moment);

		this.enrolmentRepository.save(enrolment);
	}

	public void deleteEnroll(final Enrolment enrolment) {

		Brotherhood principal;

		Assert.notNull(enrolment);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(enrolment.getBrotherhood().getId() == principal.getId() || enrolment.getMember().getId() == principal.getId());

		this.enrolmentRepository.delete(enrolment);
	}
	public void deleteEnrolldeletingProfile(final Enrolment enrolment) {

		this.enrolmentRepository.delete(enrolment);
		this.enrolmentRepository.flush();
	}

	public Enrolment findOne(final int enrolmentId) {
		Enrolment result;

		result = this.enrolmentRepository.findOne(enrolmentId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Enrolment> findAll() {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public Collection<Enrolment> findByBrotherhoodId(final int brotherhoodId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Enrolment> findByMemberId(final int memberId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findByMemberId(memberId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Enrolment> findByBrotherhoodIdAndMemberId(final int brotherhoodId, final int memberId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findByBrotherhoodIdAndMemberId(brotherhoodId, memberId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Enrolment> findAllActiveEnrolmentsByBrotherhoodId(final int brotherhoodId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findAllActiveEnrolmentsByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Enrolment> findAllActiveEnrolments() {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findAllActiveEnrolments();
		Assert.notNull(result);
		return result;
	}

	public Enrolment findActiveEnrolmentByBrotherhoodIdAndMemberId(final int brotherhoodId, final int memberId) {
		Enrolment result;

		result = this.enrolmentRepository.findActiveEnrolmentByBrotherhoodIdAndMemberId(brotherhoodId, memberId);
		return result;
	}

	public void dropOut(final int brotherhoodId) {

		Member principal;
		Enrolment enrolment;

		principal = this.memberService.findByPrincipal();
		Assert.notNull(principal);

		enrolment = this.findActiveEnrolmentByBrotherhoodIdAndMemberId(brotherhoodId, principal.getId());

		enrolment.setDropOutMoment(new Date(System.currentTimeMillis() - 1000));
	}

	public Enrolment reconstruct(final Enrolment enrolment, final Member member, final BindingResult binding) {
		Enrolment result;
		if (enrolment.getId() == 0) {
			result = enrolment;
			result.setEnrolmentMoment(new Date(System.currentTimeMillis() - 1));
			result.setBrotherhood(this.brotherhoodService.findByPrincipal());
			result.setMember(member);
		} else
			result = this.enrolmentRepository.findOne(enrolment.getId());
		result.setPosition(enrolment.getPosition());
		this.validator.validate(result, binding);
		// this.enrolmentRepository.flush();
		return result;
	}

}
