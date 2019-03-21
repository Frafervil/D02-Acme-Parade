
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	// Managed Repository

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	// Supporting services
	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private HistoryService					historyService;


	// Constructors

	public MiscellaneousRecordService() {
		super();
	}

	// Simple CRUD methods

	public MiscellaneousRecord findOne(final int id) {
		MiscellaneousRecord result;
		result = this.miscellaneousRecordRepository.findOne(id);
		return result;
	}

	public Collection<MiscellaneousRecord> findAll() {
		Collection<MiscellaneousRecord> result;
		result = this.miscellaneousRecordRepository.findAll();
		Assert.notNull(result);
		return result;
	}
	public MiscellaneousRecord create(final History history) {
		MiscellaneousRecord result;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		result = new MiscellaneousRecord();

		return result;
	}

	public void save(final MiscellaneousRecord miscellaneousRecord) {
		MiscellaneousRecord result;
		History history;
		Brotherhood principal;
		Collection<MiscellaneousRecord> miscellaneousRecords;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		result = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		Assert.notNull(result);
		miscellaneousRecords = history.getMiscellaneousRecords();

		if (!miscellaneousRecords.contains(miscellaneousRecord)) {
			miscellaneousRecords.add(miscellaneousRecord);
			history.setMiscellaneousRecords(miscellaneousRecords);
		}
		this.historyService.save(history);
	}

	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		Brotherhood principal;
		Collection<MiscellaneousRecord> miscellaneousRecords;
		History history;

		Assert.notNull(miscellaneousRecord);
		Assert.isTrue(miscellaneousRecord.getId() != 0);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		history = this.historyService.findByBrotherhoodId(principal.getId());

		miscellaneousRecords = history.getMiscellaneousRecords();

		this.miscellaneousRecordRepository.delete(miscellaneousRecord);

		miscellaneousRecords.remove(miscellaneousRecord);

		history.setMiscellaneousRecords(miscellaneousRecords);
		this.historyService.save(history);
	}
	// Other business methods
}
