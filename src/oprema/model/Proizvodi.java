package oprema.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.transaction.NotSupportedException;
/* U seterima za cijenu,kolicinu,rabat i pdv nakon setovanja promjenljive
 * pozovi potrebne funkcije za racunanje ovih transient vrijednosti
 * */

@Entity
public class Proizvodi implements Serializable{
	public static int koeficijentCuvanja=100;

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

	@Transient
	private int pozajmicaIzVelikog=0;

	@Transient
	private boolean usluge=false;

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
		return cijena/(koeficijentCuvanja+0.0);
	}

	public void setCijena(double cijena) {
		this.cijena = (int) Math.round(cijena*koeficijentCuvanja);
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

	public void setKolicina(int kolicina) throws IllegalArgumentException {
		if(kolicina<0)
			throw new IllegalArgumentException("Količina ne može biti manja od 0");
		this.kolicina = kolicina;
		postaviSve();
	}



	public int getCijenaSaRabatom() {
		return cijenaSaRabatom;
	}

	public double getCijenaSaRabatomDb(){
		return cijenaSaRabatom/(koeficijentCuvanja+0.0);
	}

	public int getCijenaPDV() {
		return cijenaPDV;
	}

	public double getCijenaPDVDb(){
		return cijenaPDV/(koeficijentCuvanja+0.0);
	}

	public int getCijenaUkupno() {
		return cijenaUkupno;
	}

	public double getCijenaUkupnoDb(){
		return cijenaUkupno/(koeficijentCuvanja+0.0);
	}


	private void postaviSaRabatom(){
		cijenaSaRabatom=(int) Math.round((cijena-((rabat/100.00)*cijena))*kolicina);
	}

	private void postaviPDV(){
		//Napisi funkciju koja izracunava kolicinu pdv-a u odnosu na njegov procenat i ukupnu cijenu sa rabatom
		//vidi iz onog racuna kako se racunaju cijena sa rabatom i kolicina pdv-a
		cijenaPDV=(int) Math.round(cijenaSaRabatom*(pdv/(100.00)));
	}

	private void postaviUkupno(){
		cijenaUkupno=cijenaSaRabatom+cijenaPDV;
	}

	public void postaviSve(){
		postaviSaRabatom();
		postaviPDV();
		postaviUkupno();
	}
	public boolean isUsluge() {
		return usluge;
	}

	public void setUsluge(boolean usluge) {
		this.usluge = usluge;
		this.rabat=0;
		this.pdv=20;
		postaviSve();
	}

	@Override
	public String toString() {
		return "Proizvodi [sifra=" + sifra + ", naziv=" + naziv + ", cijena=" + cijena + ", pdv=" + pdv + ", rabat="
				+ rabat + ", maliStanje=" + maliStanje + ", velikiStanje=" + velikiStanje + ", kolicina=" + kolicina
				+ ", cijenaSaRabatom=" + cijenaSaRabatom + ", cijenaPDV=" + cijenaPDV + ", cijenaUkupno=" + cijenaUkupno
				+ ", pozajmicaIzVelikog= "+pozajmicaIzVelikog+"]";
	}

	public void azurirajStanja() throws NotSupportedException {
		if(this.getKolicina()>(this.getMaliStanje()+this.getVelikiStanje()))
			throw new NotSupportedException("Nema dovoljno količine na stanju u magacinima"+System.lineSeparator()+"Maksimalno: "+(maliStanje+velikiStanje));
		this.setMaliStanje(this.getMaliStanje()-this.getKolicina());
		if(this.getMaliStanje()<0){
			pozajmicaIzVelikog=Math.abs(this.getMaliStanje());
			this.setVelikiStanje(this.getVelikiStanje()-pozajmicaIzVelikog);
			this.setMaliStanje(0);
		}
	}

	public void vratiStanja(){
		int stkol=this.getKolicina();
		this.setKolicina(0);
		this.setMaliStanje(this.getMaliStanje()+stkol-pozajmicaIzVelikog);
		this.setVelikiStanje(this.getVelikiStanje()+pozajmicaIzVelikog);
		pozajmicaIzVelikog=0;
	}

	public int getPozajmicaIzVelikog() {
		return pozajmicaIzVelikog;
	}

	public void setPozajmicaIzVelikog(int pozajmicaIzVelikog) {
		this.pozajmicaIzVelikog = pozajmicaIzVelikog;
	}






}
