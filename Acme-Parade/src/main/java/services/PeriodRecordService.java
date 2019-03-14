
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private PeriodRecordRepository	periodRecordRepository;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Collection<PeriodRecord> findAll() {
		Collection<PeriodRecord> result;
		result = this.periodRecordRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public PeriodRecord findOne(final int periodRecordId) {
		PeriodRecord result;
		result = this.periodRecordRepository.findOne(periodRecordId);
		return result;
	}

	// Other business method ------------------------------------------------
}
