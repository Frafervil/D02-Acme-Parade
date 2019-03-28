
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Parade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private ParadeService	paradeService;


	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 2- Manage their processions, which includes listing, showing, creating,
	 * updating, and deleting them. Processions may be saved in draft mode,
	 * which implies that they must not be shown in listings to actors other
	 * than their corresponding brother-hoods.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un actor logueado como brotherhood
	 * puede crear una parade
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como administrador
				 */
				"admin", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como hermandad
				 */
				"member1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como miembro
				 */
				"brotherhood1", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (Class<?>) createTest[i][1]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como brotherhood
	 * puede actualizar una parade
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Dejar el campo de descripción vacío
				 */
				"brotherhood1", "parade1", null, ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Introducir otra descripción
				 */
				"brotherhood1", "parade1", "other description", null
			}
		};
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0], (String) updateTest[i][1], (String) updateTest[i][2], (Class<?>) updateTest[i][3]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como brotherhood
	 * puede eliminar una parade
	 */

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Alguien sin autentificar intenta eliminar un desfile
				 */
				null, "parade1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * La hermandad al que le pertenece el desfile lo elimina
				 */
				"brotherhood1", "parade1", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], super.getEntityId((String) deleteTest[i][1]), (Class<?>) deleteTest[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.paradeService.create();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String description, final Class<?> class1) {
		Class<?> caught;
		Parade parade;

		caught = null;
		try {
			this.authenticate(actor);
			parade = this.paradeService.findOne(super.getEntityId(thing));
			parade.setDescription(description);
			this.paradeService.save(parade);
			this.unauthenticate();
			this.paradeService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final int thing, final Class<?> class1) {
		Class<?> caught;
		Parade parade;

		caught = null;
		try {
			this.authenticate(actor);
			parade = this.paradeService.findOne(thing);
			this.paradeService.delete(parade);
			//this.unauthenticate();
			this.paradeService.flush();
			Assert.isTrue(!this.paradeService.exist(parade.getId()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
