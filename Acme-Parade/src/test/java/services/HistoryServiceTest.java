
package services;

import javax.transaction.Transactional;

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
public class HistoryServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private HistoryService	historyService;


	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 3- An actor who is authenticated as a brotherhood must be able to manage their history, which includes listing,
	 * displaying, creating, updating, and deleting its records.
	 */

	// Tests

	/*
	 * En este test se va a probar que unicamente puede crear una history un actor logueado como brotherhood y que
	 * además no tenga ya una history creada.
	 */
	@Test
	public void authorityTest() {
		final Object authorityTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como administrador
				 */
				"admin", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como miembro
				 */
				"member1", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como brotherhood con historia ya creada
				 */
				"brotherhood1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como brotherhood sin historia
				 */
				"brotherhood3", null
			}
		};
		for (int i = 0; i < authorityTest.length; i++)
			this.AuthorityTemplate((String) authorityTest[i][0], (Class<?>) authorityTest[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	private void AuthorityTemplate(final String string, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(string);
			this.historyService.create();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

}
