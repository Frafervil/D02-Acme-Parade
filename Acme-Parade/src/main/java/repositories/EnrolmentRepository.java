
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.brotherhood.id = ?1")
	Collection<Enrolment> findByBrotherhoodId(int brotherhoodId);

	@Query("select e from Enrolment e where e.member.id = ?1")
	Collection<Enrolment> findByMemberId(int memberId);

	@Query("select e from Enrolment e where e.brotherhood.id = ?1 and e.member.id = ?2")
	Collection<Enrolment> findByBrotherhoodIdAndMemberId(int brotherhoodId, int memberId);

	@Query("select e from Enrolment e where e.dropOutMoment = null and e.brotherhood.id = ?1")
	Collection<Enrolment> findAllActiveEnrolmentsByBrotherhoodId(int brotherhoodId);

	@Query("select e from Enrolment e where e.dropOutMoment = null")
	Collection<Enrolment> findAllActiveEnrolments();

	@Query("select e from Enrolment e where e.dropOutMoment = null and e.brotherhood.id = ?1 and e.member.id = ?2")
	Enrolment findActiveEnrolmentByBrotherhoodIdAndMemberId(int brotherhoodId, int memberId);

}
