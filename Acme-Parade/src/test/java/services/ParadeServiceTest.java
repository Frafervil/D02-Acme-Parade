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
import domain.Parade;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class ParadeServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private ParadeService paradeService;
	// --------------------------------------------------

	@Autowired
	private BrotherhoodService brotherhoodService;

	// Tests

	@Test
	public void testFindOne() {
		Parade result;

		super.authenticate("brotherhood1");
		result = this.paradeService.findOne(286);

		System.out.println(result);
		super.unauthenticate();
	}

	@Test
	public void testCreateAndSave() {
		Parade parade, saved;
		Collection<Parade> parades;
		Brotherhood brotherhood;

		super.authenticate("brotherhood1");
		brotherhood = this.brotherhoodService.findOne(252);

		parade = this.paradeService.create();
		parade.setTitle("Título 1");
		parade.setDescription("Descripción 1");
		parade.setMoment(new Date(1531526400000L));
		parade.setMaxRow(50);
		parade.setMaxColumn(50);
		parade.setBrotherhood(brotherhood);

		saved = this.paradeService.save(parade);
		System.out.println(parade.getTicker());
		parades = paradeService.findAll();
		Assert.isTrue(parades.contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		Collection<Parade> result;

		super.authenticate("brotherhood1");
		result = this.paradeService.findAll();

		System.out.println(result.size());
		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		Parade parade;
		Collection<Parade> parades1;
		Collection<Parade> parades2;

		super.authenticate("brotherhood1");
		parade = this.paradeService.findOne(this
				.getEntityId("parade1"));
		parades1 = this.paradeService.findAll();
		this.paradeService.delete(parade);

		parades2 = this.paradeService.findAll();
		Assert.isTrue(parades2.size() != parades1.size());
		super.unauthenticate();
	}

}
