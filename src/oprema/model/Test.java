package oprema.model;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

		Proizvodi proizvod1 = new Proizvodi();
		Proizvodi proizvod2 = new Proizvodi();
		Proizvodi proizvod3 = new Proizvodi();
		Proizvodi proizvod4 = new Proizvodi();
		Proizvodi proizvod5 = new Proizvodi();
		Proizvodi proizvod6 = new Proizvodi();

		proizvod1.setNaziv("Naziv 1. proizvoda");
		proizvod2.setNaziv("Naziv 2. proizvoda");
		proizvod3.setNaziv("Naziv 3. proizvoda");
		proizvod4.setNaziv("Naziv 4. proizvoda");
		proizvod5.setNaziv("Naziv 5. proizvoda");
		proizvod6.setNaziv("Naziv 6. proizvoda");


		proizvod1.setSifra("00001");
		proizvod2.setSifra("00002");
		proizvod3.setSifra("00003");
		proizvod4.setSifra("00004");
		proizvod5.setSifra("00005");
		proizvod6.setSifra("00006");

		proizvod1.setPdv(20);
		proizvod2.setPdv(20);
		proizvod3.setPdv(20);
		proizvod4.setPdv(20);
		proizvod5.setPdv(20);
		proizvod6.setPdv(20);

		proizvod1.setRabat(150);
		proizvod2.setRabat(180);
		proizvod3.setRabat(250);
		proizvod4.setRabat(130);
		proizvod5.setRabat(210);
		proizvod6.setRabat(200);

		proizvod1.setCijena(1000);
		proizvod2.setCijena(2000);
		proizvod3.setCijena(1500);
		proizvod4.setCijena(2345);
		proizvod5.setCijena(9214);
		proizvod6.setCijena(580);


		proizvod1.setMaliStanje(3);
		proizvod1.setVelikiStanje(10);
		proizvod2.setMaliStanje(0);
		proizvod2.setVelikiStanje(40);
		proizvod3.setMaliStanje(10);
		proizvod3.setVelikiStanje(40);
		proizvod4.setMaliStanje(5);
		proizvod4.setVelikiStanje(12);
		proizvod5.setMaliStanje(30);
		proizvod5.setVelikiStanje(100);
		proizvod6.setMaliStanje(1);
		proizvod6.setVelikiStanje(13);


		SessionFactory sf = conf.buildSessionFactory();
		Session sesija = sf.openSession();


		sesija.beginTransaction();
		sesija.save(proizvod1);
		sesija.save(proizvod2);
		sesija.save(proizvod3);
		sesija.save(proizvod4);
		sesija.save(proizvod5);
		sesija.save(proizvod6);

		sesija.getTransaction().commit();

		sesija.close();
		sf.close();

	}

}
