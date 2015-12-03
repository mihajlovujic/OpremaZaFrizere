package oprema.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
/* U seterima za cijenu,kolicinu,rabat i pdv nakon setovanja promjenljive
 * pozovi potrebne funkcije za racunanje ovih transient vrijednosti
 * */

@Entity
public class Proizvodi {


	@Id
	private String sifra;
	@Column
	private String naziv;
	@Column
	private double cijena;
	@Column
	private int pdv;
	@Column
	private int rabat;
	@Column
	private int maliStanje;
	@Column
	private int velikiStanje;

	@Transient
	private int kolicina=1;

	@Transient
	private double cijenaSaRabatom;

	@Transient
	private double cijenaPDV;

	@Transient
	private double cijenaUkupno;


	public Proizvodi(){

	}

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



	public double getCijena() {
		return cijena;
	}

	public void setCijena(double cijena) {
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



	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}



	public double getCijenaSaRabatom() {
		return cijenaSaRabatom;
	}

	public double getCijenaPDV() {
		return cijenaPDV;
	}

	public double getCijenaUkupno() {
		return cijenaUkupno;
	}


	private void postaviSaRabatom(){
		cijenaSaRabatom=cijena/(rabat/(100+0.0))*kolicina;
	}

	private void postaviPDV(){
		//Napisi funkciju koja izracunava kolicinu pdv-a u odnosu na njegov procenat i ukupnu cijenu sa rabatom
		//vidi iz onog racuna kako se racunaju cijena sa rabatom i kolicina pdv-a
		cijenaPDV=cijenaSaRabatom*(pdv/(100+0.0));
	}

	private void postaviUkupno(){
		cijenaUkupno=cijenaSaRabatom+cijenaPDV;
	}

	public void postaviSve(){
		postaviSaRabatom();
		postaviPDV();
		postaviUkupno();
	}
	@Override
	public String toString() {
		return "Proizvodi [sifra=" + sifra + ", naziv=" + naziv + ", cijena=" + cijena + ", pdv=" + pdv + ", rabat="
				+ rabat + ", maliStanje=" + maliStanje + ", velikiStanje=" + velikiStanje + ", kolicina=" + kolicina
				+ ", cijenaSaRabatom=" + cijenaSaRabatom + ", cijenaPDV=" + cijenaPDV + ", cijenaUkupno=" + cijenaUkupno
				+ "]";
	}






}
