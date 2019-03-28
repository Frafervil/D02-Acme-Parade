
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import domain.Administrator;
import domain.Enrolment;
import domain.Position;

@Service
@Transactional
public class PositionService {

	// Managed Repository
	@Autowired
	private PositionRepository		positionRepository;

	// Supporting services

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private EnrolmentService		enrolmentService;


	// Simple CRUD methods

	public Position create() {
		Position result;
		Administrator principal;

		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Position();
		Assert.notNull(result);

		return result;
	}

	public Collection<Position> findAll() {
		Collection<Position> result;
		result = this.positionRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Position findOne(final int positionId) {
		Position result;

		result = this.positionRepository.findOne(positionId);

		Assert.notNull(result);

		return result;
	}

	public Position save(final Position position) {
		Position result;
		Administrator principal;

		Assert.notNull(position);
		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);

		result = this.positionRepository.save(position);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Position position) {
		Administrator principal;
		Integer positions;
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);

		positions = this.positionRepository.findUsedPosition(position.getId());

		principal = this.administratorService.findByPrincipal();

		Assert.notNull(principal);

		Assert.notNull(positions);
		Assert.isTrue(positions == 0, "There are members with this position");
		this.positionRepository.delete(position);

	}

	public Map<String, Integer> positionStats() {
		Map<String, Integer> result;
		Collection<Enrolment> enrolments;
		enrolments = this.enrolmentService.findAllActiveEnrolments();
		result = new HashMap<String, Integer>();
		int oldValue;
		String lpos;

		for (final Enrolment e : enrolments) {
			lpos = e.getPosition().getEnglishName().toString();
			if (result.containsKey(lpos)) {
				oldValue = result.get(lpos);
				result.put(lpos, oldValue + 1);
			} else
				result.put(lpos, 1);
		}
		return result;

	}
	
	public void flush(){
		this.positionRepository.flush();
	}
}
