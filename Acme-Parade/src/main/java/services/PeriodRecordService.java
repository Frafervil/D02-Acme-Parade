
package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private PeriodRecordRepository	periodRecordRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


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

	public PeriodRecord create(final History history) {
		PeriodRecord result;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		result = new PeriodRecord();

		return result;
	}

	public void save(final PeriodRecord periodRecord) {
		PeriodRecord result;
		History history;
		Brotherhood principal;
		Collection<PeriodRecord> periodRecords;
		final Integer startYear = periodRecord.getStartYear();
		final Integer endYear = periodRecord.getEndYear();
		
		final Calendar calendar = Calendar.getInstance();
		final Integer actualYear = calendar.get(Calendar.YEAR);
		
		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		history = this.historyService.findByBrotherhoodId(principal.getId());
		periodRecords = history.getPeriodRecords();

		if (!periodRecords.contains(periodRecord)) {
			periodRecords.add(periodRecord);
			history.setPeriodRecords(periodRecords);
		}
		if (startYear != null && endYear != null){
			Assert.isTrue(startYear <= actualYear, "Start year must be past");
			Assert.isTrue(endYear <= actualYear, "End year must be past");
			Assert.isTrue(startYear<=endYear, "Start year must be before than end year");
		}
		
		result = this.periodRecordRepository.save(periodRecord);
		Assert.notNull(result);
		
		this.historyService.save(history);
	}

	public void delete(final PeriodRecord periodRecord) {
		Brotherhood principal;
		Collection<PeriodRecord> periodRecords;
		History history;

		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getId() != 0);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		history = this.historyService.findByBrotherhoodId(principal.getId());

		periodRecords = history.getPeriodRecords();

		this.periodRecordRepository.delete(periodRecord);

		periodRecords.remove(periodRecord);

		history.setPeriodRecords(periodRecords);
		this.historyService.save(history);
	}

	// Other business method ------------------------------------------------
	
	public void flush(){
		this.periodRecordRepository.flush();
	}
}
