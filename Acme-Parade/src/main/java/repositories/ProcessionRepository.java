package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Procession;

@Repository
public interface ProcessionRepository extends
		JpaRepository<Procession, Integer> {

	@Query("select p from Procession p where p.brotherhood.id = ?1")
	Collection<Procession> findAllProcessionsOfOneBrotherhood(int brotherhoodId);

	@Query("select p from Procession p where p.isDraft = 0")
	Collection<Procession> findAllProcessionsFinal();

	@Query("select p from Procession p where p.isDraft = 0 AND p.brotherhood.id = ?1")
	Collection<Procession> findAllProcessionsFinalOfOneBrotherhood(int brotherhoodId);
	
	@Query("select p from Procession p where p.moment > NOW() AND p.moment < ?1")
	Collection<Procession> findSoonProcessions(Date dateMax);
}
