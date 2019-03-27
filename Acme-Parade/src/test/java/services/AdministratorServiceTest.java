
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private AdministratorService	administratorService;


	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 1- Create user accounts for new administrators.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un administrator puede crear
	 * otra cuenta de administrator
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Alguien sin loguear intenta crear una cuenta de administrador
				 */
				null, "Francisco", "Javier", "Elena", "Hacienda los Olivos", "javierelena@", "fraelefer", "fraelefer", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Un administrador crea otra cuenta de administrador
				 */
				"admin", "Francisco", "Javier", "Elena", "Hacienda los Olivos", "javierelena@", "fraelefer", "fraelefer", null
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
	 * En este test se va a probar que sólo un administrator puede modificar su propio perfil
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Un miembro intenta modificar el perfil del administrador
				 */
				"member1", "admin", "other name", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Pone otro nombre
				 */
				"admin", "admin", "other name", null
			}
		};
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0], (String) updateTest[i][1], (String) updateTest[i][2], (Class<?>) updateTest[i][3]);
	}

	/*
	 * En este test se va a probar que sólo un brotherhood puede eliminar su propio perfil
	 */

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Un miembro intenta eliminar el perfil de la brotherhood
				 */
				"member1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * La hermandad intenta eliminar su perfil
				 */
				"admin", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], (Class<?>) deleteTest[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String name, final String middleName, final String surname, final String address, final String email, final String username, final String password, final Class<?> class1) {
		Class<?> caught;
		Administrator admin;

		caught = null;
		try {
			this.authenticate(actor);
			admin = this.administratorService.create();
			admin.setName(name);
			admin.setMiddleName(middleName);
			admin.setSurname(surname);
			admin.setAddress(address);
			admin.setEmail(email);

			admin.getUserAccount().setUsername(username);
			admin.getUserAccount().setPassword(password);
			this.administratorService.save(admin);
			this.unauthenticate();
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String name, final Class<?> class1) {
		Class<?> caught;
		Administrator admin;

		caught = null;
		try {
			this.authenticate(actor);
			admin = this.administratorService.findOne(super.getEntityId(thing));
			admin.setName(name);
			this.administratorService.save(admin);
			this.unauthenticate();
			this.administratorService.flush();
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
			this.administratorService.delete();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
