package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends
		JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where p.brotherhood.id = ?1")
	Collection<Parade> findAllParadesOfOneBrotherhood(int brotherhoodId);

	@Query("select p from Parade p where p.isDraft = 0")
	Collection<Parade> findAllParadesFinal();

	@Query("select p from Parade p where p.isDraft = 0 AND p.brotherhood.id = ?1")
	Collection<Parade> findAllParadesFinalOfOneBrotherhood(int brotherhoodId);
	
	@Query("select p from Parade p where p.moment > NOW() AND p.moment < ?1")
	Collection<Parade> findSoonParades(Date dateMax);
}
