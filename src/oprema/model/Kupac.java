package oprema.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Kupac implements Serializable{
	@Column
	private String naziv;
	@Column
	private String adresa;
	@Column
	private String mjesto;
	@Id
	private String pib;


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


	public String getPib() {
		return pib;
	}


	public void setPib(String pib) {
		this.pib = pib;
	}


	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return pib.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Kupac){
			if(((Kupac)obj).getPib() == this.pib)
				return true;
		}
		return false;
	}


	@Override
	public String toString() {
		return "Kupac["+this.pib+", "+this.naziv+", "+this.mjesto+", "+this.adresa+"];";
	}






}
