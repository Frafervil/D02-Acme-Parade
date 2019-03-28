
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class FloatServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private FloatService	floatService;


	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 1- Manage their floats, which includes listing, showing, creating, updating,
	 * and deleting them.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un actor logueado como brotherhood
	 * puede crear una float
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Falta introducit un título
				 */
				"brotherhood1", null, "new description", "http://www.fotolog1994.com", ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Los campos están completos y cumplen con los requisitos
				 */
				"brotherhood1", "Newtitle", "new description", "http://www.fotolog1994.com", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (String) createTest[i][1], (String) createTest[i][2], (String) createTest[i][3], (Class<?>) createTest[i][4]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como brotherhood
	 * puede actualizar una float
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Dejar el campo de descripción vacío
				 */
				"brotherhood1", "float1", null, ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Introducir otra descripción
				 */
				"brotherhood1", "float1", "other description", null
			}
		};
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0], (String) updateTest[i][1], (String) updateTest[i][2], (Class<?>) updateTest[i][3]);
	}

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Dejar el campo de descripción vacío
				 */
				null, "float1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Introducir otra descripción
				 */
				"brotherhood1", "float1", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], (String) deleteTest[i][1], (Class<?>) deleteTest[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String title, final String description, final String picture, final Class<?> class1) {
		Class<?> caught;
		domain.Float f;

		caught = null;
		try {
			this.authenticate(actor);
			f = this.floatService.create();
			f.setTitle(title);
			f.setDescription(description);
			f.getPictures().add(picture);
			this.floatService.save(f);
			this.floatService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String description, final Class<?> class1) {
		Class<?> caught;
		domain.Float f;

		caught = null;
		try {
			this.authenticate(actor);
			f = this.floatService.findOne(super.getEntityId(thing));
			f.setDescription(description);
			this.floatService.save(f);
			this.floatService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final String thing, final Class<?> class1) {
		Class<?> caught;
		domain.Float f;

		caught = null;
		try {
			this.authenticate(actor);
			f = this.floatService.findOne(super.getEntityId(thing));
			this.floatService.delete(f);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

}
