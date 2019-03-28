
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PlaceRepository;
import repositories.RequestRepository;
import domain.Brotherhood;
import domain.Member;
import domain.Parade;
import domain.Place;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed Repository

	@Autowired
	private RequestRepository	requestRepository;

	@Autowired
	private PlaceRepository		placeRepository;

	@Autowired
	private Validator			validator;

	// Supporting services

	@Autowired
	private MemberService		memberService;

	@Autowired
	private PlaceService		placeService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Simple CRUD methods

	public Collection<Request> findAll() {
		Collection<Request> result;

		result = this.requestRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Request create(final int paradeId) {
		Request result;
		Member principal;
		Place place;
		Parade parade;

		principal = this.memberService.findByPrincipal();
		Assert.notNull(principal);

		parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);

		result = new Request();
		result.setMember(principal);
		result.setParade(parade);

		place = this.placeService.create(paradeId);
		Assert.notNull(place);
		result.setPlace(place);

		result.setStatus("PENDING");

		return result;
	}

	public Collection<Request> findByPrincipal() {
		Member principal;

		principal = this.memberService.findByPrincipal();
		Assert.notNull(principal);

		Collection<Request> result;
		result = this.requestRepository.findByMember(principal.getId());
		Assert.notNull(result);
		return result;

	}

	public Collection<Request> findByPrincipalBrotherhood(final Parade parade) {
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(parade.getBrotherhood().equals(principal));

		Collection<Request> result;
		result = this.requestRepository.findAllByParade(parade.getId());
		Assert.notNull(result);
		return result;

	}
	public void delete(final Request request) {
		final Place place;

		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		Assert.isTrue(request.getStatus().equals("PENDING"));

		place = request.getPlace();

		if (place != null)
			this.placeService.delete(place);

		this.requestRepository.delete(request);

	}
	public void deleteRequestDeletingProfile(final Request request) {
		final Place place;

		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		place = request.getPlace();

		if (place != null)
			this.placeRepository.delete(place);

		this.requestRepository.delete(request);
		this.requestRepository.flush();
	}
	public Request findOne(final int requestId) {
		Request result;

		result = this.requestRepository.findOne(requestId);
		Assert.notNull(result);
		return result;

	}
	public Request save(final Request request) {
		Request result;
		result = this.requestRepository.save(request);
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public Map<String, List<Request>> groupByStatus(final Collection<Request> requests) {
		final Map<String, List<Request>> result = new HashMap<String, List<Request>>();

		Assert.notNull(requests);
		for (final Request r : requests)
			if (result.containsKey(r.getStatus()))
				result.get(r.getStatus()).add(r);
			else {
				final List<Request> l = new ArrayList<Request>();
				l.add(r);
				result.put(r.getStatus(), l);
			}

		if (!result.containsKey("APPROVED"))
			result.put("APPROVED", new ArrayList<Request>());
		if (!result.containsKey("PENDING"))
			result.put("PENDING", new ArrayList<Request>());
		if (!result.containsKey("REJECTED"))
			result.put("REJECTED", new ArrayList<Request>());

		return result;
	}

	public Collection<Request> findAllByParade(final int paradeId) {
		Collection<Request> result;

		result = this.requestRepository.findAllByParade(paradeId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Request> findAllByPlace(final int placeId) {
		Collection<Request> result;

		result = this.requestRepository.findAllByPlace(placeId);
		Assert.notNull(result);
		return result;
	}

	public Request findByParade(final int paradeId) {
		Request result;
		result = this.requestRepository.findByParade(paradeId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Request> findAllByMember(final int memberId) {
		Collection<Request> result;

		result = this.requestRepository.findByMember(memberId);
		Assert.notNull(result);

		return result;
	}

	public Double ratioapprovedRequest() {
		final Double result;
		result = this.requestRepository.ratioapprovedRequest();
		return result;
	}
	public Double ratioRejectedRequest() {
		final Double result;
		result = this.requestRepository.ratioRejectedRequest();
		return result;
	}
	public Double ratioPendingRequest() {
		final Double result;
		result = this.requestRepository.ratioPendingRequest();
		return result;
	}

	public Collection<Request> findAllByBrotherhood(final int brotherhoodId) {
		final Collection<Request> result;
		Collection<Request> requests;
		final Collection<Parade> parades;

		result = new ArrayList<Request>();
		parades = this.paradeService.findAllParadesOfOneBrotherhood(brotherhoodId);

		for (final Parade p : parades) {
			requests = this.findAllByParade(p.getId());
			result.addAll(requests);
		}

		return result;

	}

	// Brotherhood must be able to change the status of a request they manage from "PENDING" to "REJECTED"
	public void reject(final Request r) {
		Brotherhood principal;

		Assert.notNull(r);
		Assert.isTrue(r.getId() != 0);

		final String reason = r.getRejectionReason();
		Assert.notNull(reason);
		Assert.isTrue(!reason.isEmpty());

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(this.findByPrincipalBrotherhood(r.getParade()).contains(r));
		Assert.isTrue(r.getStatus().equals("PENDING"));

		r.setStatus("REJECTED");

		this.placeRepository.delete(r.getPlace().getId());
	}

	// Brotherhood must be able to change the status of a request they manage from "PENDING" to "APPROVED"
	public void approve(final Request r) {
		Brotherhood principal;

		Assert.notNull(r);
		Assert.isTrue(r.getId() != 0);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(this.findByPrincipalBrotherhood(r.getParade()).contains(r));
		Assert.isTrue(r.getStatus().equals("PENDING"));

		r.setStatus("APPROVED");

		this.placeService.save(r.getParade().getId(), r.getPlace());
		this.requestRepository.save(r);
	}

	public void flushRequest() {
		this.requestRepository.flush();
	}

	public Integer findRepeated(final int memberId, final int paradeId) {
		Integer result;
		result = this.requestRepository.findRepeated(memberId, paradeId);

		return result;
	}

	public Request reconstruct(final Request request, final Parade parade, final BindingResult binding) {
		Request result;
		result = request;
		result.setMember(this.memberService.findByPrincipal());
		result.setStatus("PENDING");
		result.setPlace(request.getPlace());
		result.setParade(request.getParade());

		this.validator.validate(result, binding);
		this.requestRepository.flush();
		return result;
	}

	public Request reconstructApprove(final Request request, final BindingResult binding) {
		Request result;
		result = this.requestRepository.findOne(request.getId());
		result.getPlace().setcolumnP(request.getPlace().getcolumnP());
		result.getPlace().setrowP(request.getPlace().getrowP());

		this.validator.validate(result, binding);
		this.requestRepository.flush();
		return result;
	}

	public Request reconstructReject(final Request request, final BindingResult binding) {
		final Request result;
		result = this.requestRepository.findOne(request.getId());
		result.setRejectionReason(request.getRejectionReason());

		this.validator.validate(result, binding);
		this.requestRepository.flush();
		return result;
	}

}
