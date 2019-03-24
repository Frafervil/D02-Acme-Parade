
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
import domain.PeriodRecord;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class PeriodRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private HistoryService	historyService;

	@Autowired
	private PeriodRecordService	periodRecordService;

	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 3- An actor who is authenticated as a brotherhood must be able to manage their history, which includes listing,
	 * displaying, creating, updating, and deleting its records.
	 */

	// Tests

	/*
	 * Test de listado de periodRecords a partir de una history
	 */
	
	@Test
	public void listingTest() {
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		history.getPeriodRecords();
		this.unauthenticate();
		
	}
	
	/*
	 * Test de creacion de un periodRecord
	 */
	
	@Test
	public void creationTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		this.periodRecordService.create(history);
		this.periodRecordService.flush();
		this.unauthenticate();
	}
	
	/*
	 * Test de edicion de un periodRecord.
	 * Haremos pruebas con una edición correcta y otras que no cumplen restricciones.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void editionTest() {
		final Collection<String> photosPeriodRecord = this.photosPeriodRecordOK();
		final Collection<String> emptyCollection = new HashSet<>();
		final Collection<String> notUrlString = this.photosPeriodRecordWrong();
		final Object authorityTest[][] = {
			{
				/*
				 * Test positivo, todos los campos son correctos
				 */
				"titulo","descripcion",photosPeriodRecord,2005,2010, null
			}, {
				/*
				 * Test negativo:
				 * Titulo vacío
				 */
				"","descripcion",photosPeriodRecord,2005,2010, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Titulo null
				 */
				null,"descripcion",photosPeriodRecord,2005,2010, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción vacia
				 */
				"Titulo","",photosPeriodRecord,2005,2010, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción null
				 */
				"Titulo",null,photosPeriodRecord,2005,2010, ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Enlace no url
				 */
				"Titulo","Descripcion",notUrlString,2005,2010, ConstraintViolationException.class
			},{
				/*
				 * Test positivo:
				 * Enlace no url vacia
				 */
				"Titulo","Descripcion",emptyCollection,2005,2010, null
			}, {
				/*
				 * Test negativo:
				 * Años mal
				 */
				"Titulo","Descripcion",photosPeriodRecord,2010,2009, IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Años mal
				 */
				"Titulo","Descripcion",photosPeriodRecord,2010,2020, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < authorityTest.length; i++)
			this.AuthorityTemplate((String) authorityTest[i][0],(String) authorityTest[i][1],(Collection<String>) authorityTest[i][2],(Integer) authorityTest[i][3],(Integer) authorityTest[i][4], (Class<?>) authorityTest[i][5]);
	}
	
	
	/*
	 * Test de borrado de un periodRecord
	 */
	
	@Test
	public void deleteTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
		PeriodRecord pR = periodRecords.iterator().next();
		this.periodRecordService.delete(pR);
		this.periodRecordService.flush();
		this.unauthenticate();
	}
	
	
	
	// Ancillary methods ------------------------------------------------------

	private Collection<String> photosPeriodRecordOK(){
		final Collection<String> photosPeriodRecord = new HashSet<>();
		
		photosPeriodRecord.add("http://www.test1.com");
		photosPeriodRecord.add("http://www.test2.com");
		photosPeriodRecord.add("http://www.test3.com");
		
		return photosPeriodRecord;
	}
	private Collection<String> photosPeriodRecordWrong(){
		final Collection<String> photosPeriodRecord = new HashSet<>();
		
		photosPeriodRecord.add("Esto no es un enlace1");
		photosPeriodRecord.add("Esto no es un enlace2");
		
		return photosPeriodRecord;
	}
	
	private void AuthorityTemplate(final String title,final String description,final Collection<String> url,final Integer startYear,final Integer endYear, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			History history;
			this.authenticate("brotherhood1");
			history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
			Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
			PeriodRecord pR = periodRecords.iterator().next();
			pR.setTitle(title);
			pR.setDescription(description);
			pR.setPictures(url);
			pR.setStartYear(startYear);
			pR.setEndYear(endYear);
			this.periodRecordService.save(pR);
			this.periodRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
	
	
}
