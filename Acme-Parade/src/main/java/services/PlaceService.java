
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PlaceRepository;
import domain.Place;
import domain.Procession;

@Service
@Transactional
public class PlaceService {

	// Managed Repository

	@Autowired
	PlaceRepository		placeRepository;

	// Services
	@Autowired
	RequestService		requestService;
	@Autowired
	ProcessionService	processionService;


	public void delete(final Place place) {
		this.placeRepository.delete(place);
	}
	public Place create(final int processionId) {
		final Place result = new Place();
		int rowMax;
		int columMax;
		int r = 1;
		int c = 1;
		Procession procession;
		Collection<Place> placesInUse;

		placesInUse = this.findByProcessionId(processionId);
		procession = this.processionService.findOne(processionId);
		rowMax = procession.getMaxRow();
		columMax = procession.getMaxColumn();

		if (placesInUse != null && (placesInUse.size() != 0))
			outerloop: for (r = 1; r <= rowMax; r++)
				for (final Place p : placesInUse)
					if (p.getrowP() != r)
						for (c = 1; c <= columMax; c++)
							if (p.getcolumnP() != c)
								break outerloop;
		result.setcolumnP(c);
		result.setrowP(r);
		return result;
	}

	public Collection<Place> findByProcessionId(final int processionId) {
		Collection<Place> result;
		result = this.placeRepository.findPlacesByProcession(processionId);
		return result;
	}
	public void save(final Place place) {
		Place result;
		result = this.placeRepository.save(place);
		Assert.notNull(result);

	}

	public void flushPlace() {
		this.placeRepository.flush();
	}
}
