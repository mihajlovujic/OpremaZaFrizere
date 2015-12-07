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
	private int cijena;
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
	private int cijenaSaRabatom;

	@Transient
	private int cijenaPDV;

	@Transient
	private int cijenaUkupno;


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



	public int getCijena() {
		return cijena;
	}

	public double getCijenaDb(){
		return cijena/(100.00);
	}

	public void setCijena(double cijena) {
		this.cijena = (int) Math.round(cijena*100);
		postaviSve();
	}

	public int getPdv() {
		return pdv;
	}

	public void setPdv(int pdv) {
		this.pdv = pdv;
		postaviSve();
	}

	public int getRabat() {
		return rabat;
	}

	public void setRabat(int rabat) {
		this.rabat = rabat;
		postaviSve();
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
		postaviSve();
	}



	public int getCijenaSaRabatom() {
		return cijenaSaRabatom;
	}

	public double getCijenaSaRabatomDb(){
		return cijenaSaRabatom/100.00;
	}

	public int getCijenaPDV() {
		return cijenaPDV;
	}

	public double getCijenaPDVDb(){
		return cijenaPDV/100.00;
	}

	public int getCijenaUkupno() {
		return cijenaUkupno;
	}

	public double getCijenaUkupnoDb(){
		return cijenaUkupno/100.00;
	}


	private void postaviSaRabatom(){
		cijenaSaRabatom=(int) Math.round(cijena/(rabat/(100+0.0))*kolicina);
	}

	private void postaviPDV(){
		//Napisi funkciju koja izracunava kolicinu pdv-a u odnosu na njegov procenat i ukupnu cijenu sa rabatom
		//vidi iz onog racuna kako se racunaju cijena sa rabatom i kolicina pdv-a
		cijenaPDV=(int) Math.round(cijenaSaRabatom*(pdv/(100+0.0)));
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
