
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import domain.Brotherhood;
import domain.Parade;
import domain.Request;

@Service
@Transactional
public class ParadeService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ParadeRepository	paradeRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private FloatService		floatService;

	@Autowired
	private Validator			validator;


	// Additional functions
	private String generateTicker() {
		String result;
		final Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR));
		year = year.substring(year.length() - 2, year.length());
		String date = String.valueOf(now.get(Calendar.DATE));
		date = date.length() == 1 ? "0".concat(date) : date;
		String month = String.valueOf(now.get(Calendar.MONTH) + 1);
		month = month.length() == 1 ? "0".concat(month) : month;
		final Random r = new Random();
		final char a = (char) (r.nextInt(26) + 'a');
		final char b = (char) (r.nextInt(26) + 'a');
		final char c = (char) (r.nextInt(26) + 'a');
		final char d = (char) (r.nextInt(26) + 'a');
		final char f = (char) (r.nextInt(26) + 'a');
		final char g = (char) (r.nextInt(26) + 'a');
		String code = String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + String.valueOf(d) + String.valueOf(f) + String.valueOf(g);
		code = code.toUpperCase();
		result = year + month + date + "-" + code;
		return result;
	}

	// Simple CRUD Methods

	public boolean exist(final Integer paradeId) {
		return this.paradeRepository.exists(paradeId);
	}

	public Parade create() {
		Parade result;
		final Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		result = new Parade();
		result.setTicker(this.generateTicker());
		result.setIsDraft(true);
		result.setBrotherhood(principal);
		return result;
	}

	public Parade findOne(final int paradeId) {
		Parade result;

		result = this.paradeRepository.findOne(paradeId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Parade> findAll() {
		Collection<Parade> result;

		result = this.paradeRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public void delete(final Parade parade) {
		Brotherhood principal;
		Collection<Request> requests;
		Collection<domain.Float> floats;

		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		requests = this.requestService.findAllByParade(parade.getId());

		for (final Request r : requests)
			this.requestService.deleteRequestDeletingProfile(r);

		floats = this.floatService.findAll();
		for (final domain.Float f : floats)
			if (f.getParade() != null)
				if (f.getParade().getId() == parade.getId())
					f.setParade(null);

		this.paradeRepository.delete(parade.getId());
	}

	// Business Methods
	public Collection<Parade> findAllParadesOfOneBrotherhood(final int brotherhoodId) {
		Collection<Parade> result;

		result = this.paradeRepository.findAllParadesOfOneBrotherhood(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Parade> findAllFinal() {
		Collection<Parade> result;

		result = this.paradeRepository.findAllParadesFinal();
		Assert.notNull(result);
		return result;
	}

	public Collection<Parade> startingSoonParades() {
		final Collection<Parade> result;
		final Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, 30);
		final Date dateMax = c.getTime();

		result = this.paradeRepository.findSoonParades(dateMax);
		Assert.notNull(result);

		return result;

	}

	public Collection<Parade> findAllFinalOfOneBrotherhood(final int brotherhoodId) {
		Collection<Parade> result;

		result = this.paradeRepository.findAllParadesFinalOfOneBrotherhood(brotherhoodId);
		Assert.notNull(result);
		return result;
	}

	public Parade findOneByRequestId(final int requestId) {
		Parade result;
		result = this.paradeRepository.findOneByRequestId(requestId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Parade> findVisibleParades(final Brotherhood brotherhood) {
		final Collection<Parade> result = this.findAllFinalOfOneBrotherhood(brotherhood.getId());
		Collection<Parade> allParades;
		final String userNameOfBrotherhood = brotherhood.getUserAccount().getUsername();

		allParades = this.findAll();

		for (final Parade p : allParades)
			if (p.getIsDraft() == true && (userNameOfBrotherhood.equals(p.getBrotherhood().getUserAccount().getUsername())))
				result.add(p);
		return result;
	}

	public Parade save(final Parade parade) {
		Brotherhood principal;
		Parade result;

		Assert.notNull(parade);

		principal = this.brotherhoodService.findByPrincipal();
		Assert.notNull(principal);

		parade.setIsDraft(false);

		result = this.paradeRepository.save(parade);
		Assert.notNull(result);

		return result;
	}

	public Parade saveAsDraft(final Parade parade) {
		Parade result;
		Assert.notNull(parade);

		parade.setIsDraft(true);
		Assert.isTrue(parade.getIsDraft());
		result = this.paradeRepository.save(parade);
		Assert.notNull(result);
		return result;
	}

	public Parade reconstruct(final Parade parade, final BindingResult binding) {
		Parade result;
		if (parade.getId() == 0) {
			result = parade;
			result.setTicker(this.generateTicker());

		} else
			result = this.paradeRepository.findOne(parade.getId());

		result.setDescription(parade.getDescription());
		result.setMaxColumn(parade.getMaxColumn());
		result.setMaxRow(parade.getMaxRow());
		result.setMoment(parade.getMoment());
		result.setTitle(parade.getTitle());
		result.setBrotherhood(this.brotherhoodService.findByPrincipal());
		this.validator.validate(result, binding);
		return result;
	}

	public Collection<Parade> findAllAvailableRequest(final int memberId) {
		Collection<Parade> result;
		Collection<Parade> parades;
		parades = this.findAllFinal();
		result = this.findAllFinal();

		for (final Parade p : parades)
			if ((this.requestService.findRepeated(memberId, p.getId())) > 0)
				result.remove(p);
		return result;
	}

	public void flush() {
		this.paradeRepository.flush();
	}
}
