
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
import domain.LinkRecord;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class LinkRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private HistoryService	historyService;

	@Autowired
	private LinkRecordService	linkRecordService;

	// --------------------------------------------------

	/*
	 * Requirement Tested:
	 * 3- An actor who is authenticated as a brotherhood must be able to manage their history, which includes listing,
	 * displaying, creating, updating, and deleting its records.
	 * 
	 * Code coverage LinkRecordService: 77,9%
	 * 
	 */

	// Tests

	/*
	 * Test de listado de linkRecords a partir de una history
	 */
	
	@Test
	public void listingTest() {
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		history.getLinkRecords();
		this.unauthenticate();
		
	}
	
	/*
	 * Test de creacion de un linkRecord
	 */
	
	@Test
	public void creationTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		this.linkRecordService.create(history);
		this.linkRecordService.flush();
		this.unauthenticate();
	}
	
	/*
	 * Test de edicion de un linkRecord.
	 * Haremos pruebas con una edición correcta y otras que no cumplen restricciones.
	 */
	@Test
	public void editionTest() {
		final Object authorityTest[][] = {
			{
				/*
				 * Test positivo, todos los campos son correctos
				 */
				"titulo","descripcion","http://www.enlace.com", null
			}, {
				/*
				 * Test negativo:
				 * Titulo vacío
				 */
				"","descripcion","http://www.enlace.com", ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Titulo null
				 */
				null,"descripcion","http://www.enlace.com", ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción vacia
				 */
				"Titulo","","http://www.enlace.com", ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Descripción null
				 */
				"Titulo",null,"http://www.enlace.com", ConstraintViolationException.class
			}, {
				/*
				 * Test negativo:
				 * Enlace no url
				 */
				"Titulo","Descripcion","Esto no es una URL", ConstraintViolationException.class
			},{
				/*
				 * Test positivo:
				 * Enlace no url vacia
				 */
				"Titulo","Descripcion","", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < authorityTest.length; i++)
			this.AuthorityTemplate((String) authorityTest[i][0],(String) authorityTest[i][1],(String) authorityTest[i][2], (Class<?>) authorityTest[i][3]);
	}
	
	
	/*
	 * Test de borrado de un periodRecord
	 */
	
	@Test
	public void deleteTest(){
		History history;
		this.authenticate("brotherhood1");
		history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
		Collection<LinkRecord> linkRecords = history.getLinkRecords();
		LinkRecord lR = linkRecords.iterator().next();
		this.linkRecordService.delete(lR);
		this.linkRecordService.flush();
		this.unauthenticate();
	}
	
	
	
	// Ancillary methods ------------------------------------------------------
	
	private void AuthorityTemplate(final String title,final String description,final String url, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			History history;
			this.authenticate("brotherhood1");
			history = this.historyService.findByBrotherhoodId(super.getEntityId("brotherhood1"));
			Collection<LinkRecord> linkRecords = history.getLinkRecords();
			LinkRecord lR = linkRecords.iterator().next();
			lR.setTitle(title);
			lR.setDescription(description);
			lR.setLinkBrotherhood(url);
			this.linkRecordService.save(lR);
			this.linkRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
	
	
}
