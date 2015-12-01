package oprema.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
		conf.addAnnotatedClass(Proizvodi.class);
		sf=conf.buildSessionFactory();
	}
	public Session getSession(){
		if(s==null)
			s=sf.openSession();
		return s;
	}

	public void vratiSesiju(){
		try{
			s.getTransaction().commit();
		}
		catch(HibernateException h){

		}
		s.close();
	}

	public void zatvoriKlasu(){
		if(s!=null)
			s.close();
		sf.close();
	}
	public List<Proizvodi> getProizvodSifraLike(String sifra){
		List<Proizvodi> proiz=(List<Proizvodi>) getSession().createCriteria(Proizvodi.class).add(Restrictions.like("sifra", sifra));
		return proiz;
	}

	public Proizvodi getProizvodPoSifri(String sifra){
		return (Proizvodi) getSession().get(Proizvodi.class, sifra);
	}
}
