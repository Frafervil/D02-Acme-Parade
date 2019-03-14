
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private LegalRecordRepository	legalRecordRepository;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Collection<LegalRecord> findAll() {
		Collection<LegalRecord> result;
		result = this.legalRecordRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public LegalRecord findOne(final int legalRecordId) {
		LegalRecord result;
		result = this.legalRecordRepository.findOne(legalRecordId);
		return result;
	}

	// Other business method ------------------------------------------------
}
