
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AreaRepository;
import domain.Area;

@Service
@Transactional
public class AreaService {

	// Managed Repository

	@Autowired
	AreaRepository	areaRepository;


	// Services

	//Simple CRUD methods
	public Area create() {
		final Area result;
		result = new Area();

		return result;
	}

	public void delete(final Area area) {
		this.areaRepository.delete(area);
	}

	public void save(final Area area) {
		Area result;
		result = this.areaRepository.save(area);
		Assert.notNull(result);

	}

	public Area findOne(final int areaId) {
		Area result;

		result = this.areaRepository.findOne(areaId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Area> findAll() {
		Collection<Area> result;

		result = this.areaRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	//Other bussiness methods

	public Collection<Area> findAreasWithNoBrotherhood() {
		Collection<Area> result;

		result = this.areaRepository.findAll();
		Assert.notNull(result);
		return result;
	}
}
