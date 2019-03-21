
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private LinkRecordRepository	linkRecordRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


	// Simple CRUD methods ----------------------------------------------------

	public Collection<LinkRecord> findAll() {
		Collection<LinkRecord> result;
		result = this.linkRecordRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public LinkRecord findOne(final int linkRecordId) {
		LinkRecord result;
		result = this.linkRecordRepository.findOne(linkRecordId);
		return result;
	}
	public LinkRecord create(final History history) {
		LinkRecord result;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		result = new LinkRecord();

		return result;
	}

	public void save(final LinkRecord linkRecord) {
		LinkRecord result;
		History history;
		Brotherhood principal;
		Collection<LinkRecord> linkRecords;

		principal = this.brotherhoodService.findByPrincipal();
		history = this.historyService.findByBrotherhoodId(principal.getId());

		result = this.linkRecordRepository.save(linkRecord);
		Assert.notNull(result);
		linkRecords = history.getLinkRecords();

		if (!linkRecords.contains(linkRecord)) {
			linkRecords.add(linkRecord);
			history.setLinkRecords(linkRecords);
		}
		this.historyService.save(history);
	}

	public void delete(final LinkRecord linkRecord) {
		Brotherhood principal;
		Collection<LinkRecord> linkRecords;
		History history;

		Assert.notNull(linkRecord);
		Assert.isTrue(linkRecord.getId() != 0);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		history = this.historyService.findByBrotherhoodId(principal.getId());

		linkRecords = history.getLinkRecords();

		this.linkRecordRepository.delete(linkRecord);

		linkRecords.remove(linkRecord);

		history.setLinkRecords(linkRecords);
		this.historyService.save(history);
	}
	// Other business method ------------------------------------------------
}
