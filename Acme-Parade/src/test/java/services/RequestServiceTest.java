
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Parade;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private RequestService	requestService;
	// --------------------------------------------------

	@Autowired
	private ParadeService	paradeService;


	/*
	 * Requirement Tested:
	 * 6- Manage the request to march on a procession, which includes listing
	 * them by status, showing them, and deciding on them. When the decision
	 * on a pending request is to accept it, the brotherhood must provide a
	 * position in the procession, which is identi-fied by means of a row and
	 * a column; the system must check that no two members can march at
	 * the same row/column; the system must suggest a good position auto-matically,
	 * but the brotherhood may change it. When the decision is to reject it,
	 * the brotherhood must provide an explanation.
	 */

	// Tests

	/*
	 * En este test se va a probar que un actor logueado como brotherhood
	 * sólo puede ver las requests de las parades que gestiona
	 */

	@Test
	public void listingTest() {
		final Object listTest[][] = {
			{
				/*
				 * Test negativo:
				 * Busca solicitudes de un desfile que no gestiona
				 */
				"brotherhood1", "parade2", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Busca solicitudes de un desfile que gestiona
				 */
				"brotherhood1", "parade1", null
			}
		};
		for (int i = 0; i < listTest.length; i++)
			this.ListTemplate((String) listTest[i][0], (String) listTest[i][1], (Class<?>) listTest[i][2]);
	}

	/*
	 * Test de mostrar una request
	 */

	@Test
	public void displayTest() {
		final Object displayTest[][] = {
			{
				/*
				 * Test positivo:
				 * La solicitud existe
				 */
				"brotherhood1", "request1", null
			}
		};
		for (int i = 0; i < displayTest.length; i++)
			this.DisplayTemplate((String) displayTest[i][0], (String) displayTest[i][1], (Class<?>) displayTest[i][2]);
	}

	/*
	 * Test de aprobar una request de una parade que gestione un actor logueado
	 * como brotherhood
	 */

	@Test
	public void approveTest() {
		final Object approveTest[][] = {
			{
				/*
				 * Test negativo:
				 * Esta solicitud no puede ser aprobada por la hermandad logueada
				 */
				"brotherhood2", "request10", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Esta solicitud puede ser aprobada por la hermandad logueada
				 */
				"brotherhood1", "request10", null
			}
		};
		for (int i = 0; i < approveTest.length; i++)
			this.ApproveTemplate((String) approveTest[i][0], (String) approveTest[i][1], (Class<?>) approveTest[i][2]);
	}

	/*
	 * Test de rechazar una request de una parade que gestione un actor logueado
	 * como brotherhood y proporcione una reject reason
	 */

	@Test
	public void rejectTest() {
		final Object rejectTest[][] = {
			{
				/*
				 * Test negativo:
				 * No da una razón para el rechazo
				 */
				"brotherhood1", "request10", null, IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Da una razón para el rechazo
				 */
				"brotherhood1", "request10", "Lamentamos decirle que...", null
			}
		};
		for (int i = 0; i < rejectTest.length; i++)
			this.RejectTemplate((String) rejectTest[i][0], (String) rejectTest[i][1], (String) rejectTest[i][2], (Class<?>) rejectTest[i][3]);
	}

	// Ancillary methods ------------------------------------------------------

	private void ListTemplate(final String actor, final String thing, final Class<?> class1) {
		Class<?> caught;
		Collection<Request> requests;

		caught = null;
		try {
			this.authenticate(actor);
			final Parade parade = this.paradeService.findOne(super.getEntityId(thing));
			requests = this.requestService.findByPrincipalBrotherhood(parade);
			this.requestService.groupByStatus(requests);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DisplayTemplate(final String actor, final String thing, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.requestService.findOne(super.getEntityId(thing));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void ApproveTemplate(final String actor, final String thing, final Class<?> class1) {
		Class<?> caught;
		Request request;

		caught = null;
		try {
			this.authenticate(actor);
			request = this.requestService.findOne(super.getEntityId(thing));
			this.requestService.approve(request);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void RejectTemplate(final String actor, final String thing, final String reason, final Class<?> class1) {
		Class<?> caught;
		Request request;

		caught = null;
		try {
			this.authenticate(actor);
			request = this.requestService.findOne(super.getEntityId(thing));
			request.setRejectionReason(reason);
			this.requestService.reject(request);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	/*
	 * Requirement Tested:
	 * 1- Manage his or her requests to march on a procession,
	 * which includes listing them by status, showing, creating them,
	 * and deleting them. Note that the requests cannot be updated,
	 * but they can be deleted as long as they are in the pending status.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un actor logueado como member
	 * puede crear una request
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como administrador
				 */
				"admin", "parade1", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como hermandad
				 */
				"brotherhood1", "parade1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como miembro
				 */
				"member3", "parade1", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (String) createTest[i][1], (Class<?>) createTest[i][2]);
	}

	/*
	 * En este test se va a probar que sólo se pueden borrar request en status pending
	 */

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Esta solicitud ya ha sido rechazada
				 */
				"member2", "request2", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Esta solicitud ya ha sido aceptada
				 */
				"member1", "request3", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Esta solicitud está en estado "pendiente"
				 */
				"member2", "request10", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], (String) deleteTest[i][1], (Class<?>) deleteTest[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String thing, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.requestService.create(super.getEntityId(thing));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final String thing, final Class<?> class1) {
		Class<?> caught;
		Request request;

		caught = null;
		try {
			this.authenticate(actor);
			request = this.requestService.findOne(super.getEntityId(thing));
			this.requestService.delete(request);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
