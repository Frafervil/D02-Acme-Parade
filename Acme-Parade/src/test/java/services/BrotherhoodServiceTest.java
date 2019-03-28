
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Area;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private AreaService			areaService;


	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 1- Register to the system as a member or a brotherhood.
	 */

	// Tests

	/*
	 * En este test se va a probar que se deben rellenar los campos obligatorios para
	 * registrarse como brotherhood
	 */

	@SuppressWarnings("unchecked")
	@Test
	public void createTest() {
		final Collection<String> photosPeriodRecord = this.photosPeriodRecordOK();

		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Falta introducir un nombre
				 */
				null, null, "Javier", "Elena", "Hacienda los Olivos", "javierelena@gmail.com", "new brotherhood", "http://www.fotolog626.com", photosPeriodRecord, "fraelefer", "fraelefer", "area1", ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Los campos están completos y cumplen con los requisitos
				 */
				null, "Francisco", "Javier", "Elena", "Hacienda los Olivos", "javierelena@gmail.com", "new brotherhood", "http://www.fotolog626.com", photosPeriodRecord, "fraelefer", "fraelefer", "area1", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (String) createTest[i][1], (String) createTest[i][2], (String) createTest[i][3], (String) createTest[i][4], (String) createTest[i][5], (String) createTest[i][6], (String) createTest[i][7],
				(Collection<String>) createTest[i][8], (String) createTest[i][9], (String) createTest[i][10], (String) createTest[i][11], (Class<?>) createTest[i][12]);
	}

	/*
	 * Requirement Tested:
	 * 2- Edit his or her personal data.
	 */

	/*
	 * En este test se va a probar que sólo un brotherhood puede modificar su propio perfil
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Un miembro intenta modificar el perfil de la hermandad
				 */
				"member1", "brotherhood1", "other name", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Pone otro nombre
				 */
				"brotherhood1", "brotherhood1", "other name", null
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
				"brotherhood1", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], (Class<?>) deleteTest[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String name, final String middleName, final String surname, final String address, final String email, final String title, final String photo, final Collection<String> pictures,
		final String username, final String password, final String areaName, final Class<?> class1) {
		Class<?> caught;
		Brotherhood brotherhood;

		caught = null;
		try {
			this.authenticate(actor);
			brotherhood = this.brotherhoodService.create();
			brotherhood.setName(name);
			brotherhood.setMiddleName(middleName);
			brotherhood.setSurname(surname);
			brotherhood.setAddress(address);
			brotherhood.setEmail(email);
			brotherhood.setTitle(title);
			brotherhood.setPhoto(photo);
			brotherhood.setPictures(pictures);

			final Area area = this.areaService.findOne(super.getEntityId(areaName));
			brotherhood.setArea(area);

			brotherhood.getUserAccount().setUsername(username);
			brotherhood.getUserAccount().setPassword(password);
			this.brotherhoodService.save(brotherhood);
			this.unauthenticate();
			this.brotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String name, final Class<?> class1) {
		Class<?> caught;
		Brotherhood brotherhood;

		caught = null;
		try {
			this.authenticate(actor);
			brotherhood = this.brotherhoodService.findOne(super.getEntityId(thing));
			brotherhood.setName(name);
			this.brotherhoodService.save(brotherhood);
			this.unauthenticate();
			this.brotherhoodService.flush();
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
			this.brotherhoodService.delete();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	// Ancillary methods ------------------------------------------------------

	private Collection<String> photosPeriodRecordOK() {
		final Collection<String> photosPeriodRecord = new HashSet<>();

		photosPeriodRecord.add("http://www.test1.com");
		photosPeriodRecord.add("http://www.test2.com");
		photosPeriodRecord.add("http://www.test3.com");

		return photosPeriodRecord;
	}
}
