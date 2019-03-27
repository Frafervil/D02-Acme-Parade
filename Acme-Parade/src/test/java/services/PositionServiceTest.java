
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class PositionServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private PositionService			positionService;
	// --------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	/*
	 * Requirement Tested:
	 * 2- Manage the catalogue of positions, which includes listing, showing,
	 * creating, updat-ing, and deleting them. Positions can be deleted as long as
	 * they are not used.
	 */

	// Tests

	/*
	 * En este test se va a probar que unicamente puede crear una position
	 * un actor logueado como administrator
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como hermandad
				 */
				"brotherhood1", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como miembro
				 */
				"member1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como administrador
				 */
				"admin", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (Class<?>) createTest[i][1]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como administrator
	 * puede actualizar una position
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Un miembro intenta modificar una position
				 */
				"member1", "position1", "otro nombre en español", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Un administrador intenta modificar una position
				 */
				"admin", "position1", "otro nombre en español", null
			}
		};
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0], (String) updateTest[i][1], (String) updateTest[i][2], (Class<?>) updateTest[i][3]);
	}

	/*
	 * En este test se va a probar que sólo se puede eliminar una position
	 * si no está siendo usado por nadie
	 */

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Intentar eliminar una posición usada
				 */
				"admin", "position1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Intentar eliminar una posición no usada
				 */
				"admin", "position4", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], (String) deleteTest[i][1], (Class<?>) deleteTest[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.positionService.create();
			this.unauthenticate();
			this.positionService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String spanishTrad, final Class<?> class1) {
		Class<?> caught;
		Position position;

		caught = null;
		try {
			this.authenticate(actor);
			position = this.positionService.findOne(super.getEntityId(thing));
			position.setSpanishName(spanishTrad);
			this.positionService.save(position);
			this.unauthenticate();
			this.positionService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final String thing, final Class<?> class1) {
		Class<?> caught;
		Position position;

		caught = null;
		try {
			this.authenticate(actor);
			position = this.positionService.findOne(super.getEntityId(thing));
			this.positionService.delete(position);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
