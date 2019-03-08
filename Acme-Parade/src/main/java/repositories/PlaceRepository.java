
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

	@Query("select p.place from Request p where p.procession.id = ?1")
	Collection<Place> findPlacesByProcession(int processionId);

}
