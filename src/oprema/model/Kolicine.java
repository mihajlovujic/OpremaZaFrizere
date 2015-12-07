package oprema.model;

public class Kolicine {
	private int glavnica=0,pdvU=0,ukupno=0;

	public Kolicine(){

	}

	public Kolicine(Proizvodi p) {
		this.glavnica=p.getCijenaSaRabatom();
		this.pdvU=p.getCijenaPDV();
		this.ukupno=p.getCijenaUkupno();
	}

	public int getGlavnica() {
		return glavnica;
	}

	public void setGlavnica(int glavnica) {
		this.glavnica = glavnica;
	}

	public int getPdvU() {
		return pdvU;
	}

	public void setPdvU(int pdvU) {
		this.pdvU = pdvU;
	}

	public int getUkupno() {
		return ukupno;
	}

	public void setUkupno(int ukupno) {
		this.ukupno = ukupno;
	}



}
