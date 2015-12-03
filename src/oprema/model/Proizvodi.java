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



	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public int getCijenaSaRabatom() {
		return cijenaSaRabatom;
	}

	public void setCijenaSaRabatom(int cijenaSaRabatom) {
		this.cijenaSaRabatom = cijenaSaRabatom;
	}

	public int getCijenaPDV() {
		return cijenaPDV;
	}

	public void setCijenaPDV(int cijenaPDV) {
		this.cijenaPDV = cijenaPDV;
	}

	public int getCijenaUkupno() {
		return cijenaUkupno;
	}

	public void setCijenaUkupno(int cijenaUkupno) {
		this.cijenaUkupno = cijenaUkupno;
	}

	private void postaviSaRabatom(){
		cijenaSaRabatom=cijena/(rabat/100)*kolicina;
	}

	private void postaviPDV(){
		//Napisi funkciju koja izracunava kolicinu pdv-a u odnosu na njegov procenat i ukupnu cijenu sa rabatom
		//vidi iz onog racuna kako se racunaju cijena sa rabatom i kolicina pdv-a
		cijenaPDV=cijenaSaRabatom*pdv/100;
	}

	private void postaviUkupno(){
		cijenaUkupno=cijenaSaRabatom+cijenaPDV;
	}

	@Override
	public String toString() {
		return "Proizvodi [sifra=" + sifra + ", naziv=" + naziv + ", cijena=" + cijena + ", pdv=" + pdv + ", rabat="
				+ rabat + ", maliStanje=" + maliStanje + ", velikiStanje=" + velikiStanje + ", kolicina=" + kolicina
				+ ", cijenaSaRabatom=" + cijenaSaRabatom + ", cijenaPDV=" + cijenaPDV + ", cijenaUkupno=" + cijenaUkupno
				+ "]";
	}






}
