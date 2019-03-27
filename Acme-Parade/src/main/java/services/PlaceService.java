
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PlaceRepository;
import domain.Parade;
import domain.Place;

@Service
@Transactional
public class PlaceService {

	// Managed Repository

	@Autowired
	PlaceRepository	placeRepository;

	// Services
	@Autowired
	RequestService	requestService;
	@Autowired
	ParadeService	paradeService;


	public void delete(final Place place) {
		this.placeRepository.delete(place);
	}
	public Place create(final int paradeId) {
		final Place result = new Place();
		int rowMax;
		int columMax;
		int r = 1;
		int c = 1;
		Parade parade;
		Collection<Place> placesInUse;

		placesInUse = this.findByParadeId(paradeId);
		parade = this.paradeService.findOne(paradeId);
		rowMax = parade.getMaxRow();
		columMax = parade.getMaxColumn();

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

	public Collection<Place> findByParadeId(final int paradeId) {
		Collection<Place> result;
		result = this.placeRepository.findPlacesByParade(paradeId);
		return result;
	}
	public void save(final int paradeId, final Place place) {
		Place result;
		result = this.placeRepository.save(place);
		Assert.notNull(result);

	}

	public Integer findRepeated(final int paradeId, final int rowP, final int columnP) {
		Integer result;
		result = this.placeRepository.findRepeatedPlace(paradeId, rowP, columnP);

		return result;
	}

	public void flushPlace() {
		this.placeRepository.flush();
	}
}
