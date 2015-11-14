package oprema.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Proizvodi {


	@Id
	private int sifra;

	private String naziv;

	private int cijena;

	private int pdv;

	private int rabat;

	public int getSifra() {
		return sifra;
	}

	public void setSifra(int sifra) {
		this.sifra = sifra;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public int getCijena() {
		return cijena;
	}

	public void setCijena(int cijena) {
		this.cijena = cijena;
	}

	public int getPdv() {
		return pdv;
	}

	public void setPdv(int pdv) {
		this.pdv = pdv;
	}

	public int getRabat() {
		return rabat;
	}

	public void setRabat(int rabat) {
		this.rabat = rabat;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getNaziv()+"";
	}


}
