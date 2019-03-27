
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class MemberServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private MemberService	memberService;


	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 1- Register to the system as a member or a brotherhood.
	 */

	// Tests

	/*
	 * En este test se va a probar que se deben rellenar los campos obligatorios para
	 * registrarse como member
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Falta introducir un nombre
				 */
				null, null, "Javier", "Elena", "Hacienda los Olivos", "javierelena@gmail.com", "fraelefer", "fraelefer", ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Los campos están completos y cumplen con los requisitos
				 */
				null, "Francisco", "Javier", "Elena", "Hacienda los Olivos", "javierelena@gmail.com", "fraelefer", "fraelefer", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (String) createTest[i][1], (String) createTest[i][2], (String) createTest[i][3], (String) createTest[i][4], (String) createTest[i][5], (String) createTest[i][6], (String) createTest[i][7],
				(Class<?>) createTest[i][8]);
	}

	/*
	 * Requirement Tested:
	 * 2- Edit his or her personal data.
	 */

	/*
	 * En este test se va a probar que sólo un member puede modificar su propio perfil
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Una hermandad intenta modificar el perfil del miembro
				 */
				"brotherhood1", "member1", "other name", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Poner otro nombre
				 */
				"member1", "member1", "other name", null
			}
		};
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0], (String) updateTest[i][1], (String) updateTest[i][2], (Class<?>) updateTest[i][3]);
	}

	/*
	 * En este test se va a probar que sólo un member puede eliminar su propio perfil
	 */

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Una hermandad intenta eliminar el perfil del miembro
				 */
				"brotherhood1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * El miembro intenta eliminar su perfil
				 */
				"member1", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], (Class<?>) deleteTest[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String name, final String middleName, final String surname, final String address, final String email, final String username, final String password, final Class<?> class1) {
		Class<?> caught;
		Member member;

		caught = null;
		try {
			this.authenticate(actor);
			member = this.memberService.create();
			member.setName(name);
			member.setMiddleName(middleName);
			member.setSurname(surname);
			member.setAddress(address);
			member.setEmail(email);
			member.getUserAccount().setUsername(username);
			member.getUserAccount().setPassword(password);
			this.memberService.save(member);
			this.unauthenticate();
			this.memberService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String name, final Class<?> class1) {
		Class<?> caught;
		Member member;

		caught = null;
		try {
			this.authenticate(actor);
			member = this.memberService.findOne(super.getEntityId(thing));
			member.setName(name);
			this.memberService.save(member);
			this.unauthenticate();
			this.memberService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.memberService.delete();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
