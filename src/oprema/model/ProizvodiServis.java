package oprema.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class ProizvodiServis {

	private SessionFactory sf;
	private Session s;

	public ProizvodiServis(){
		Configuration conf = new Configuration();
		conf.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		conf.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		File tren=new File(".");
		String putanja=tren.getAbsoluteFile().getParent()+File.separator+"opremaDB";
		conf.setProperty("hibernate.connection.url", "jdbc:h2:"+putanja);
		conf.setProperty("hibernate.connection.username", "ognjen");
		conf.setProperty("hibernate.connection.password", "");
		conf.setProperty("hibernate.hbm2ddl.auto", "update");
		conf.setProperty("hibernate.connection.autocommit","true");
		conf.setProperty("hibernate.show_sql", "true");
		conf.addAnnotatedClass(Proizvodi.class);
		conf.addAnnotatedClass(Kupac.class);
		sf=conf.buildSessionFactory();
	}
	public Session getSession(){
		if(s==null)
			s=sf.openSession();
		return s;
	}

	public void vratiSesiju(){
		try{
			s.flush();
			s.getTransaction().commit();
		}
		catch(HibernateException h){

		}
		s.close();
		s=null;
	}

	public void zatvoriKlasu(){
		if(s!=null)
			s.close();
		sf.close();
	}
	public List<Proizvodi> getProizvodSifraLike(String sifra){
		List<Proizvodi> proiz=(List<Proizvodi>) getSession().createCriteria(Proizvodi.class).add(Restrictions.like("sifra", sifra, MatchMode.START).ignoreCase()).list();
		vratiSesiju();
		return proiz;
	}

	public List<Proizvodi> getProizvodNazivLike(String naziv){
		List<Proizvodi> proiz=(List<Proizvodi>)getSession().createCriteria(Proizvodi.class).add(Restrictions.like("naziv", naziv, MatchMode.ANYWHERE).ignoreCase()).list();
		vratiSesiju();
		return proiz;
	}

	public Proizvodi getProizvodPoSifri(String sifra){

		Proizvodi trazeni=(Proizvodi) getSession().get(Proizvodi.class, sifra);
		if(trazeni!=null){
			trazeni.postaviSve();
		}
		vratiSesiju();
		return trazeni;
	}

	public void izbrisiProizvod(Proizvodi p){
		Proizvodi trazeni=(Proizvodi)getSession().get(Proizvodi.class, p.getSifra());
		if(trazeni==null)
			return;
		getSession().delete(trazeni);
		vratiSesiju();
	}

	public void apdejtuj(Proizvodi p){
		Proizvodi trazeni=(Proizvodi)getSession().get(Proizvodi.class, p.getSifra());
		if(trazeni==null){
			getSession().save(p);
			vratiSesiju();
			return;
		}
		/*trazeni.setCijena(p.getCijenaDb());
		trazeni.setRabat(p.getRabat());
		trazeni.setPdv(p.getPdv());
		trazeni.setMaliStanje(p.getMaliStanje());
		trazeni.setVelikiStanje(p.getVelikiStanje());
		trazeni.setNaziv(p.getNaziv());
		getSession().save(trazeni);*/
		getSession().merge(p);
		vratiSesiju();
	}

	public List<Proizvodi> sviProizvodi(){
		List<Proizvodi> svi=(List<Proizvodi>)getSession().createCriteria(Proizvodi.class).list();
		vratiSesiju();
		return svi;
	}

	public List<Kupac> sviKupci(){
		List<Kupac> svi=getSession().createCriteria(Kupac.class).list();
		vratiSesiju();
		return svi;
	}
	public Kupac poPIBu(String pib){
		Kupac rez=getSession().get(Kupac.class, pib);
		vratiSesiju();
		return rez;
	}

	public void izbrisiKupca(String pib){
		getSession().delete(getSession().get(Kupac.class, pib));
		vratiSesiju();
	}

	public void apdejtujKupca(Kupac k){
		getSession().saveOrUpdate(k);
		vratiSesiju();
	}

	public List<Kupac> poPibuLike(String pib){
		List<Kupac> rez=getSession().createCriteria(Kupac.class).add(Restrictions.like("pib", pib, MatchMode.ANYWHERE)).list();
		vratiSesiju();
		return rez;
	}

	public List<Kupac> poNazivuLike(String naziv){
		List<Kupac> rez=getSession().createCriteria(Kupac.class).add(Restrictions.like("naziv", naziv, MatchMode.START).ignoreCase()).list();
		vratiSesiju();
		return rez;
	}
	public void apdejtujKolicine(Proizvodi a) {
		Proizvodi trazeni=(Proizvodi)getSession().get(Proizvodi.class, a.getSifra());
		if(trazeni==null){
			vratiSesiju();
			return;
		}
		/*trazeni.setCijena(p.getCijenaDb());
		trazeni.setRabat(p.getRabat());
		trazeni.setPdv(p.getPdv());
		trazeni.setMaliStanje(p.getMaliStanje());
		trazeni.setVelikiStanje(p.getVelikiStanje());
		trazeni.setNaziv(p.getNaziv());
		getSession().save(trazeni);*/
		trazeni.setMaliStanje(a.getMaliStanje());
		trazeni.setVelikiStanje(a.getVelikiStanje());
		getSession().save(trazeni);
		vratiSesiju();
	}

}
