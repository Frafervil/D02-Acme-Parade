
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Enrolment;
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class EnrolmentServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private EnrolmentService	enrolmentService;
	/*
	 *  Percentage of service tested: 34,1 %
	 * 
	 */
	// --------------------------------------------------

	@Autowired
	private MemberService		memberService;

	/*
	 * Requirement Tested:
	 * 3- Manage the members of the brotherhood, which includes listing, showing,
	 * enrolling, and removing them. When a member is enrolled, a position must be
	 * selected by the brotherhood.
	 */

	// Tests

	/*
	 * En este test se va a probar que unicamente puede hacer enrol
	 * (afiliar) un actor logueado como brotherhood
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como administrador
				 */
				"admin", "member1", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como miembro
				 */
				"member2", "member1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como hermandad
				 */
				"brotherhood1", "member1", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (String) createTest[i][1], (Class<?>) createTest[i][2]);
	}

	/*
	 * Test de listado de members
	 */

	@Test
	public void listingTest() {
		final Object listTest[][] = {
			{
				/*
				 * Test negativo:
				 * Busca una lista que no existe
				 */
				"brotherhood1", "member1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como hermandad
				 */
				"brotherhood1", "brotherhood2", null
			}
		};
		for (int i = 0; i < listTest.length; i++)
			this.ListTemplate((String) listTest[i][0], (String) listTest[i][1], (Class<?>) listTest[i][2]);
	}

	/*
	 * Test de mostrar un member
	 */

	@Test
	public void displayTest() {
		final Object displayTest[][] = {
			{
				/*
				 * Test positivo:
				 * El miembro existe
				 */
				"brotherhood1", "member1", null
			}
		};
		for (int i = 0; i < displayTest.length; i++)
			this.DisplayTemplate((String) displayTest[i][0], (String) displayTest[i][1], (Class<?>) displayTest[i][2]);
	}

	/*
	 * Test de eliminar members
	 */

	@Test
	public void deleteAsBrotherhoodTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como administrador
				 */
				"administrator1", "member2", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como miembro
				 */
				"member2", "member2", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como hermandad
				 */
				"brotherhood1", "member2", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteAsBrotherhoodTemplate((String) deleteTest[i][0], (String) deleteTest[i][1], (Class<?>) deleteTest[i][2]);
	}

	/*
	 * Requirement Tested:
	 * 2- Drop out from a brotherhood to which he or she belongs.
	 * The system must record the moment then the drop out takes place.
	 * A member may be re-enrolled after he or she drops out.
	 */

	@Test
	public void dropOutAsMemberTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como administrador
				 */
				"admin", "brotherhood1", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como miembro
				 */
				"brotherhood1", "brotherhood1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como hermandad
				 */
				"member2", "brotherhood1", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DropOutAsMemberTemplate((String) deleteTest[i][0], (String) deleteTest[i][1], (Class<?>) deleteTest[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor1, final String actor2, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor1);
			final Member member = this.memberService.findOne(super.getEntityId(actor2));
			this.enrolmentService.create(member);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void ListTemplate(final String actor1, final String actor2, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor1);
			this.memberService.findAllActiveMembersOfOneBrotherhood(super.getEntityId(actor2));
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DisplayTemplate(final String actor1, final String actor2, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor1);
			this.memberService.findOne(super.getEntityId(actor2));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteAsBrotherhoodTemplate(final String actor1, final String actor2, final Class<?> class1) {
		Class<?> caught;
		Enrolment enrolment;

		caught = null;
		try {
			this.authenticate(actor1);
			enrolment = this.enrolmentService.findActiveEnrolmentByBrotherhoodIdAndMemberId(super.getEntityId(actor1), super.getEntityId(actor2));
			this.enrolmentService.delete(enrolment);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DropOutAsMemberTemplate(final String actor1, final String actor2, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor1);
			this.enrolmentService.dropOut(super.getEntityId(actor2));
			this.enrolmentService.findActiveEnrolmentByBrotherhoodIdAndMemberId(super.getEntityId(actor2), super.getEntityId(actor1));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

}
