package oprema.model;

import java.util.HashMap;
import java.util.Map;

public class Obracun {
	private Map<String, Kolicine> proizvodi=new HashMap<>(150);
	private int glavnicaU=0,pdvU=0,ukupnoSve=0;

	public Obracun(){

	}

	public void stavi(Proizvodi p){
		if(p==null)
			return;
		Kolicine novi=new Kolicine(p);
		Kolicine stari=proizvodi.put(p.getSifra(), novi);

		glavnicaU+=novi.getGlavnica();
		pdvU+=novi.getPdvU();
		ukupnoSve+=novi.getUkupno();

		if(stari!=null){
			glavnicaU-=stari.getGlavnica();
			pdvU-=stari.getPdvU();
			ukupnoSve-=stari.getUkupno();
		}
	}

	public void izbaci(Proizvodi p){
		if(p==null)
			return;
		Kolicine stari=proizvodi.remove(p.getSifra());
		if(stari!=null){
			glavnicaU-=stari.getGlavnica();
			pdvU-=stari.getPdvU();
			ukupnoSve-=stari.getUkupno();
		}
	}

	public boolean sadrzi(Proizvodi p){
		return proizvodi.get(p.getSifra())!=null;
	}
	public double getGlavnicaUDb() {
		return glavnicaU/(Proizvodi.koeficijentCuvanja+0.0);
	}


	public double getPdvUDb() {
		return pdvU/(Proizvodi.koeficijentCuvanja+0.0);
	}


	public double getUkupnoSve() {
		return ukupnoSve/(Proizvodi.koeficijentCuvanja+0.0);
	}

	public void isprazni() {
		proizvodi.clear();
		glavnicaU=0;
		pdvU=0;
		ukupnoSve=0;
	}






}
