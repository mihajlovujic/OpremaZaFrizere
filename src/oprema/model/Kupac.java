package oprema.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Kupac {
	@Column
	private String naziv;
	@Column
	private String adresa;
	@Column
	private String mjesto;
	@Id
	private int pib;


	public Kupac()
	{

	}


	public String getNaziv() {
		return naziv;
	}


	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}


	public String getAdresa() {
		return adresa;
	}


	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}


	public String getMjesto() {
		return mjesto;
	}


	public void setMjesto(String mjesto) {
		this.mjesto = mjesto;
	}


	public int getPib() {
		return pib;
	}


	public void setPib(int pib) {
		this.pib = pib;
	}




}
