
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	// Managed Repository

	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;


	public InceptionRecord create() {
		InceptionRecord result;

		result = new InceptionRecord();

		return result;
	}

	public void save(final InceptionRecord inceptionRecord) {
		InceptionRecord result;
		result = this.inceptionRecordRepository.save(inceptionRecord);
		Assert.notNull(result);
	}

}
