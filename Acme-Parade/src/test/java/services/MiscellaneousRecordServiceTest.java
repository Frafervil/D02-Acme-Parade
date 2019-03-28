
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.History;
import domain.MiscellaneousRecord;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private HistoryService	historyService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 3- An actor who is authenticated as a brotherhood must be able to manage their history, which includes listing,
	 * displaying, creating, updating, and deleting its records.
	 * 
	 * Code coverage MiscellaneousRecordService: 77,9%
	 * 
	 */

	// Tests

	/*
	 * Test de listado de MiscellaneousRecord a partir de una history
	 */
	
	@Test
	public void listingTest() {
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		history.getMiscellaneousRecords();
		this.unauthenticate();
		
	}
	
	/*
	 * Test de creacion de un MiscellaneousRecord
	 */
	
	@Test
	public void creationTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		this.miscellaneousRecordService.create(history);
		this.miscellaneousRecordService.flush();
		this.unauthenticate();
	}
	
	/*
	 * Test de edicion de un MiscellaneousRecord.
	 * Haremos pruebas con una edición correcta y otras que no cumplen restricciones.
	 */
	@Test
	public void editionTest() {
		final Object authorityTest[][] = {
			{
				/*
				 * Test positivo, todos los campos son correctos
				 */
				"titulo","descripcion", null
			}, {
				/*
				 * Test negativo:
				 * Titulo vacío
				 */
				"","descripcion", ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Titulo null
				 */
				null,"descripcion", ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción vacia
				 */
				"Titulo","", ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción null
				 */
				"Titulo",null, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < authorityTest.length; i++)
			this.AuthorityTemplate((String) authorityTest[i][0],(String) authorityTest[i][1], (Class<?>) authorityTest[i][2]);
	}
	
	
	/*
	 * Test de borrado de un periodRecord
	 */
	
	@Test
	public void deleteTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();
		MiscellaneousRecord mR = miscellaneousRecords.iterator().next();
		this.miscellaneousRecordService.delete(mR);
		this.miscellaneousRecordService.flush();
		this.unauthenticate();
	}
	
	
	
	// Ancillary methods ------------------------------------------------------

	private void AuthorityTemplate(final String title,final String description, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			History history;
			this.authenticate("brotherhood1");
			history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
			Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();
			MiscellaneousRecord mR = miscellaneousRecords.iterator().next();
			mR.setTitle(title);
			mR.setDescription(description);
			this.miscellaneousRecordService.save(mR);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
	
	
}
