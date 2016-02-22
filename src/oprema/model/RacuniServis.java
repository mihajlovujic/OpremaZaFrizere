package oprema.model;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import oprema.aplikacija.HBoxRacunChooser.Akcija;

public class RacuniServis {
	private static String putanjaDoRacuna=ProizvodiServis.putanjaDoPodataka+"racuni"+File.separator;
	static{
		File f=new File(putanjaDoRacuna);

		System.out.println(f.toPath());
		if(!f.exists()){
			try {
				Files.createDirectory(f.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static List<Proizvodi> ucitaniProizvodi=null;
	public static Kupac ucitaniKupac=null;
	public static String ucitaniRacun=null;
	public static Akcija akcija = null;
	public static ProizvodiServis ps = null;
	public static List<Proizvodi> izbaceniProizvodi= new ArrayList<>();

	public static List<String> napravljeniRacuni(){
		List<String> rezultat=new ArrayList<>();
		File folder=new File(putanjaDoRacuna);
		List<File> fajlovi = Arrays.asList(folder.listFiles());
		fajlovi.sort(new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				// TODO Auto-generated method stub
				return (int) (o2.lastModified() - o1.lastModified());
			}
		});
		for(File rac : fajlovi){
			if(rac.getName().endsWith(".rac")){
				rezultat.add(rac.getName().substring(0, rac.getName().indexOf(".rac")));
			}
		}
		return rezultat;

	}

	public static boolean dodajRacun(List<Proizvodi> proizvodi, Kupac kupac,String imeFajla){

		if(proizvodi==null || kupac==null || imeFajla==null || imeFajla.isEmpty()){
			return false;
		}
		System.out.println("Proslijedjena lista: "+proizvodi);

		if(imeFajla.indexOf(".xls") != -1){
			imeFajla=imeFajla.substring(0, imeFajla.indexOf(".xls"));
		}
		if(akcija.equals(Akcija.IZMJENI)){
		}
		DateFormat df=new SimpleDateFormat("dd-MM-yy HH:mm");

		String putanja=putanjaDoRacuna+imeFajla+(akcija.equals(Akcija.IZMJENI) ? "" : ("("+(df.format(new Date()))+")"))+".rac";
		File fajl=new File(putanja);
		fajl.getParentFile().mkdirs();
		PrintWriter print=null;
		try {
			print=new PrintWriter(fajl);

			print.println(String.format("%s, %s, %s, %s", kupac.getPib(), kupac.getNaziv(), kupac.getMjesto(), kupac.getAdresa()));

			for(Proizvodi p : proizvodi){
				print.println(String.format("%s, %f, %d, %d, %d, %d, %b",p.getSifra(), p.getCijenaDb(), p.getRabat(), p.getPdv(), p.getKolicina(), p.getPozajmicaIzVelikog(), p.isUsluge()));

			}

			print.flush();
			print.close();
			return true;
		} catch (Exception e) {
			File f=new File(putanja);
			if(f.exists()){
				try {
					Files.delete(f.toPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			return false;
		}
		finally{

		}

	}

	public static void ucitajRacun(String ime){
		String putanja=putanjaDoRacuna+ime+".rac";
		try {
			List<String> linije=Files.readAllLines((new File(putanja)).toPath());
			Kupac k=new Kupac();
			String[] polja=linije.get(0).split(",");
			k.setPib(polja[0].trim());
			k.setNaziv(polja[1].trim());
			k.setMjesto(polja[2].trim());
			k.setAdresa(polja[3].trim());
			List<Proizvodi> ucitani=new ArrayList<>();
			for(int i=1;i<linije.size();i++){
				Proizvodi tren=new Proizvodi();
				String[] poljaP=linije.get(i).split(",");

				tren.setSifra(poljaP[0].trim());
				tren.setCijena(Double.parseDouble(poljaP[1].trim()));
				tren.setRabat(Integer.parseInt(poljaP[2].trim()));
				tren.setPdv(Integer.parseInt(poljaP[3].trim()));
				tren.setKolicina(Integer.parseInt(poljaP[4].trim()));
				tren.setPozajmicaIzVelikog(Integer.parseInt(poljaP[5].trim()));
				tren.setUsluge(Boolean.parseBoolean(poljaP[6].trim()));
				ucitani.add(tren);
			}
			System.out.println(ucitani);
			ucitaniProizvodi = ucitani;
			ucitaniKupac = k;
			ucitaniRacun = ime;
			System.out.println(k);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void izbrisi(String ime) {
		ucitajRacun(ime);
		izbrisiKolicine(ucitaniProizvodi);
		ucitaniProizvodi = null;
		ucitaniKupac = null;
		ucitaniRacun = null;
		try {
			Files.delete((new File(putanjaDoRacuna+ime+".rac")).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void izbrisiKolicine(List<Proizvodi> ucitaniProizvodi2) {
		for(Proizvodi p : ucitaniProizvodi2){
			Proizvodi proc = ps.getProizvodPoSifri(p.getSifra());
			if(proc!=null){
				proc.setMaliStanje(proc.getMaliStanje()+p.getKolicina()-p.getPozajmicaIzVelikog());
				proc.setVelikiStanje(proc.getVelikiStanje()+p.getPozajmicaIzVelikog());
				ps.apdejtujKolicine(proc);
			}
		}


	}

}
