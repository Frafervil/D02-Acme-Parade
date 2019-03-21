
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private LegalRecordRepository	legalRecordRepository;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


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

	public LegalRecord create(final History history) {
		LegalRecord result;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		result = new LegalRecord();

		return result;
	}

	public void save(final LegalRecord legalRecord) {
		LegalRecord result;
		History history;
		Brotherhood principal;
		Collection<LegalRecord> legalRecords;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		result = this.legalRecordRepository.save(legalRecord);
		Assert.notNull(result);
		legalRecords = history.getLegalRecords();

		if (!legalRecords.contains(legalRecord)) {
			legalRecords.add(legalRecord);
			history.setLegalRecords(legalRecords);
		}
		this.historyService.save(history);
	}

	public void delete(final LegalRecord legalRecord) {
		Brotherhood principal;
		Collection<LegalRecord> legalRecords;
		History history;

		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		history = this.historyService.findByBrotherhoodId(principal.getId());

		legalRecords = history.getLegalRecords();

		this.legalRecordRepository.delete(legalRecord);

		legalRecords.remove(legalRecord);

		history.setLegalRecords(legalRecords);
		this.historyService.save(history);
	}
	// Other business method ------------------------------------------------
}
