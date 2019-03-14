
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import domain.History;

@Service
@Transactional
public class HistoryService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private HistoryRepository	historyRepository;


	// Supporting services ----------------------------------------------------

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
}
