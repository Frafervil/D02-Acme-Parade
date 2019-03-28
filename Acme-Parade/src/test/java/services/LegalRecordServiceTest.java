
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

import domain.History;
import domain.LegalRecord;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class LegalRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private HistoryService	historyService;

	@Autowired
	private LegalRecordService	legalRecordService;

	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 3- An actor who is authenticated as a brotherhood must be able to manage their history, which includes listing,
	 * displaying, creating, updating, and deleting its records.
	 * 
	 * Code coverage LegalRecordSerice: 77,9%
	 * 
	 */

	// Tests

	/*
	 * Test de listado de legalRecords a partir de una history
	 */
	
	@Test
	public void listingTest() {
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		history.getLegalRecords();
		this.unauthenticate();
		
	}
	
	/*
	 * Test de creacion de un legalRecord
	 */
	
	@Test
	public void creationTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		this.legalRecordService.create(history);
		this.legalRecordService.flush();
		this.unauthenticate();
	}
	
	/*
	 * Test de edicion de un legalRecord.
	 * Haremos pruebas con una edición correcta y otras que no cumplen restricciones.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void editionTest() {
		final Collection<String> testingLaws = this.testingLaws();
		final Object authorityTest[][] = {
			{
				/*
				 * Test positivo, todos los campos son correctos
				 */
				"titulo","descripcion","nombre de ley",2.0,testingLaws, null
			}, {
				/*
				 * Test negativo:
				 * Titulo vacío
				 */
				"","descripcion","nombre de ley",2.0,testingLaws, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Titulo null
				 */
				null,"descripcion","nombre de ley",2.0,testingLaws, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción vacia
				 */
				"Titulo","","nombre de ley",2.0,testingLaws, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción null
				 */
				"Titulo",null,"nombre de ley",2.0,testingLaws, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * vatNumber null
				 */
				"Titulo","Descripcion","nombre de ley",null,testingLaws, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * legalName null
				 */
				"Titulo","Descripcion",null,2.0,testingLaws, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * legalName vacio
				 */
				"Titulo","Descripcion","",2.0,testingLaws, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < authorityTest.length; i++)
			this.AuthorityTemplate((String) authorityTest[i][0],(String) authorityTest[i][1],(String) authorityTest[i][2],(Double) authorityTest[i][3],(Collection<String>) authorityTest[i][4], (Class<?>) authorityTest[i][5]);
	}
	
	
	/*
	 * Test de borrado de un periodRecord
	 */
	
	@Test
	public void deleteTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		Collection<LegalRecord> legalRecords = history.getLegalRecords();
		LegalRecord lR = legalRecords.iterator().next();
		this.legalRecordService.delete(lR);
		this.legalRecordService.flush();
		this.unauthenticate();
	}
	
	
	
	// Ancillary methods ------------------------------------------------------

	private Collection<String> testingLaws(){
		final Collection<String> testLaws = new HashSet<>();
		
		testLaws.add("Esto es la ley 1");
		testLaws.add("Esto es la ley 2");
		testLaws.add("Esto es la ley 3");
		
		return testLaws;
	}
	
	private void AuthorityTemplate(final String title,final String description,final String legalName,final Double vatNumber,final Collection<String> applicableLaws, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			History history;
			this.authenticate("brotherhood1");
			history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
			Collection<LegalRecord> legalRecords = history.getLegalRecords();
			LegalRecord lR = legalRecords.iterator().next();
			lR.setTitle(title);
			lR.setDescription(description);
			lR.setLegalName(legalName);
			lR.setVatNumber(vatNumber);
			lR.setApplicableLaws(applicableLaws);
			this.legalRecordService.save(lR);
			this.legalRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
	
	
}
