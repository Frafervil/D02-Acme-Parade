
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FloatRepository;
import domain.Brotherhood;
import domain.Float;

@Service
@Transactional
public class FloatService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private FloatRepository		floatRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private Validator			validator;


	// Additional functions

	// Simple CRUD Methods

	public domain.Float create() {

		Brotherhood principal;
		domain.Float result;
		Collection<String> pictures;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		result = new domain.Float();
		pictures = new ArrayList<String>();

		result.setPictures(pictures);
		result.setBrotherhood(principal);

		return result;
	}

	public domain.Float save(final domain.Float floatB) {

		Brotherhood principal;
		domain.Float result;

		Assert.notNull(floatB);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(floatB.getBrotherhood().equals(principal));

		result = this.floatRepository.save(floatB);
		Assert.notNull(result);

		return result;
	}

	public void delete(final domain.Float floatB) {

		Brotherhood principal;

		Assert.notNull(floatB);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		this.floatRepository.delete(floatB);
	}

	public domain.Float findOne(final int floatId) {
		domain.Float result;

		result = this.floatRepository.findOne(floatId);
		Assert.notNull(result);
		return result;

	}

	public Collection<domain.Float> findAll() {
		Collection<domain.Float> result;

		result = this.floatRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public Collection<domain.Float> findByBrotherhoodId(final int brotherhoodId) {
		Collection<domain.Float> result;

		result = this.floatRepository.findByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Float reconstruct(final Float floatB, final BindingResult binding) {
		Float result;
		if (floatB.getId() == 0)
			result = floatB;

		else
			result = this.floatRepository.findOne(floatB.getId());
		result.setTitle(floatB.getTitle());
		result.setDescription(floatB.getDescription());
		result.setPictures(floatB.getPictures());
		result.setParade(floatB.getParade());
		result.setBrotherhood(this.brotherhoodService.findByPrincipal());
		this.validator.validate(result, binding);
		this.floatRepository.flush();
		return result;
	}
	
	public void flush(){
		this.floatRepository.flush();
	}
}
