
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class DashboardServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private HistoryService	historyService;


	// --------------------------------------------------

	// Tests

	/*
	 * Requirement Tested:
	 * 4. An actor who is authenticated as an administrator must be able to:
	 * 1. Display a dashboard.
	 * 
	 * En este test probamos que unicamente un usuario logueado como admin puede hacer uso
	 * de los servicios del dashboard.
	 * Creamos un driver con distintos tipos de usuarios y lo probamos.
	 */

	@Test
	public void authorityTest() {
		final Object authorityTest[][] = {
			{
				"admin", null
			}, {
				"member1", IllegalArgumentException.class
			}, {
				"brotherhood1", IllegalArgumentException.class
			}, {
				null, IllegalArgumentException.class
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
			this.historyService.avgRecordPerHistory();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	/*
	 * Requirement Tested:
	 * Probamos que los resultados devueltos por los test del calculo de estadisticas
	 * sean los esperados en base a nuestro populate.
	 */

	@Test
	public void valueTest() {
		final Object valueTest[][] = {
			{
				"avg", 3.5, null
			}, {
				"max", 2.0, null
			}, {
				"min", 5.0, null
			}, {
				"stddev", 1.5, null
			}, {
				"avg", 0.0, IllegalArgumentException.class
			}, {
				"min", 0.0, IllegalArgumentException.class
			}, {
				"max", 0.0, IllegalArgumentException.class
			}, {
				"stddev", 0.0, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < valueTest.length; i++)
			this.ValueTemplate((String) valueTest[i][0], (Double) valueTest[i][1], (Class<?>) valueTest[i][2]);

	}

	private void ValueTemplate(final String string, final Double double1, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		Double value = 0.0;
		try {
			this.authenticate("admin");
			if (string == "avg") {
				value = this.historyService.avgRecordPerHistory();
				Assert.isTrue(value.equals(double1));
			} else if (string == "min") {
				value = this.historyService.minRecordPerHistory();
				Assert.isTrue(value.equals(double1));
			} else if (string == "max") {
				value = this.historyService.maxRecordPerHistory();
				Assert.isTrue(value.equals(double1));
			} else if (string == "stddev") {
				value = this.historyService.stddevRecordPerHistory();
				Assert.isTrue(value.equals(double1));
			}
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
