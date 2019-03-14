
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private LinkRecordRepository	linkRecordRepository;


	// Supporting services ----------------------------------------------------

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

	// Other business method ------------------------------------------------
}
