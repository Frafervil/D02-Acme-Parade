package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "rowP",
		"columnP" }) })
public class Place extends DomainEntity {

	private int rowP;
	private int columnP;

	@NotNull
	@Range(min = 1)
	public int getrowP() {
		return this.rowP;
	}

	public void setrowP(final int rowP) {
		this.rowP = rowP;
	}

	@NotNull
	@Range(min = 1)
	public int getcolumnP() {
		return this.columnP;
	}

	public void setcolumnP(final int columnP) {
		this.columnP = columnP;
	}

}
