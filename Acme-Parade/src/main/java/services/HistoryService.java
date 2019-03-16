
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.History;

@Service
@Transactional
public class HistoryService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private HistoryRepository	historyRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService		actorService;


	// Simple CRUD Methods

	public History findOne(final int historyId) {
		History result;

		result = this.historyRepository.findOne(historyId);
		Assert.notNull(result);
		return result;
	}

	public Collection<History> findAll() {
		Collection<History> result;

		result = this.historyRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public History findByBrotherhoodId(final int brotherhoodId) {
		History result;

		result = this.historyRepository.findByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Double avgRecordPerHistory() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.avgRecordPerHistory();
	}

	public Double maxRecordPerHistory() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.maxRecordPerHistory();
	}

	public Double minRecordPerHistory() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.minRecordPerHistory();
	}

	public Double stddevRecordPerHistory() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		final Collection<History> histories = this.historyRepository.findAll();
		Double sum = 0.0;
		final Double med = this.historyRepository.avgRecordPerHistory();
		Double result = 0.0;
		if (!histories.isEmpty()) {
			for (final History h : histories)
				sum = sum + Math.pow(this.recordsPerBrotherhoodId(h.getBrotherhood().getId()) - med, 2);
			result = Math.sqrt(sum / (histories.size()));
		}
		return result;
	}

	private double recordsPerBrotherhoodId(final int id) {
		return this.historyRepository.recordsPerBrotherhoodId(id);
	}

	public Brotherhood largestBrotherhood() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.largestBrotherhood();
	}

	public Collection<Brotherhood> brotherhoodsMoreThanAverage() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		return this.historyRepository.brotherhoodsMoreThanAverage();
	}

}