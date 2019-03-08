
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;

@Repository
public interface BrotherhoodRepository extends JpaRepository<Brotherhood, Integer> {

	@Query("select a from Brotherhood a where a.userAccount.id = ?1")
	Brotherhood findByUserAccountId(int userAccountId);

	@Query("select b from Brotherhood b where b.area.id = ?1")
	Brotherhood findByAreaId(int areaId);

	@Query("select e.brotherhood from Enrolment e where e.member.id = ?1")
	Collection<Brotherhood> findAllBrotherhoodsOfOneMember(int memberId);

}
