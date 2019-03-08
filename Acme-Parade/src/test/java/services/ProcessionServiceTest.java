package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Brotherhood;
import domain.Procession;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class ProcessionServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private ProcessionService processionService;
	// --------------------------------------------------

	@Autowired
	private BrotherhoodService brotherhoodService;

	// Tests

	@Test
	public void testFindOne() {
		Procession result;

		super.authenticate("brotherhood1");
		result = this.processionService.findOne(286);

		System.out.println(result);
		super.unauthenticate();
	}

	@Test
	public void testCreateAndSave() {
		Procession procession, saved;
		Collection<Procession> processions;
		Brotherhood brotherhood;

		super.authenticate("brotherhood1");
		brotherhood = this.brotherhoodService.findOne(252);

		procession = this.processionService.create();
		procession.setTitle("Título 1");
		procession.setDescription("Descripción 1");
		procession.setMoment(new Date(1531526400000L));
		procession.setMaxRow(50);
		procession.setMaxColumn(50);
		procession.setBrotherhood(brotherhood);

		saved = this.processionService.save(procession);
		System.out.println(procession.getTicker());
		processions = processionService.findAll();
		Assert.isTrue(processions.contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		Collection<Procession> result;

		super.authenticate("brotherhood1");
		result = this.processionService.findAll();

		System.out.println(result.size());
		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		Procession procession;
		Collection<Procession> processions1;
		Collection<Procession> processions2;

		super.authenticate("brotherhood1");
		procession = this.processionService.findOne(this
				.getEntityId("procession1"));
		processions1 = this.processionService.findAll();
		this.processionService.delete(procession);

		processions2 = this.processionService.findAll();
		Assert.isTrue(processions2.size() != processions1.size());
		super.unauthenticate();
	}

}
