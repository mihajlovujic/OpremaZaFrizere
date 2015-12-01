package oprema.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Proizvodi {


	@Id
	private String sifra;
	@Column
	private String naziv;
	@Column
	private int cijena;
	@Column
	private int pdv;
	@Column
	private int rabat;
	@Column
	private int maliStanje;
	@Column
	private int velikiStanje;

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
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

	public int getMaliStanje() {
		return maliStanje;
	}

	public void setMaliStanje(int maliStanje) {
		this.maliStanje = maliStanje;
	}

	public int getVelikiStanje() {
		return velikiStanje;
	}

	public void setVelikiStanje(int velikiStanje) {
		this.velikiStanje = velikiStanje;
	}

	@Override
	public String toString() {
		return "Proizvodi [sifra=" + sifra + ", naziv=" + naziv + ", cijena=" + cijena + ", pdv=" + pdv + ", rabat="
				+ rabat + ", maliStanje=" + maliStanje + ", velikiStanje=" + velikiStanje + "]";
	}




}
