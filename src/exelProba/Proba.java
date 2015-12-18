package exelProba;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import oprema.model.Kupac;
import oprema.model.Proizvodi;
import oprema.model.ProizvodiServis;

public class Proba {

	public static void main(String[] args) throws IOException {
		ProizvodiServis ps=new ProizvodiServis();
		List<Proizvodi> lista=ps.sviProizvodi();
		for(Proizvodi a : lista){
			a.postaviSve();
		}
		Kupac kupac=ps.poPIBu("12345678");
		InputStream is=Proba.class.getClassLoader().getResourceAsStream("racun 01-M-2015 boza 2.xls");
		Workbook wb=new HSSFWorkbook(is);
		Sheet sh=wb.getSheetAt(0);
		Row[] ukupno=rezultatRedovi(sh);
		Row zaglavlje=zaglavlje(sh);
		Row[] tabela=tabela(sh);

		upisiKupca(sh, kupac);

		upisiProizvode(sh, lista, tabela);

		upisiCijene(sh,lista, ukupno);

//		sh.shiftRows(29, sh.getLastRowNum(), -13);
		FileOutputStream out=new FileOutputStream("proba2.xls");
		wb.write(out);
		out.flush();
		out.close();
		is.close();
		ps.zatvoriKlasu();
	}

	private static void upisiCijene(Sheet sh, List<Proizvodi> lista, Row[] ukupno) {
		int prviRed=30+lista.size()+9;


		for(int i=0;i<ukupno.length;i++){
			Row tren=sh.createRow(prviRed+i);
			for(int k=0;k<8;k++){
				if(k==7)
					continue;
				if(ukupno[i].getCell(k)!=null){
					Cell c=tren.createCell(k);
					c.setCellStyle(ukupno[i].getCell(k).getCellStyle());
					if(k==6 && i!=3){
						c.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						c.setCellValue(500000.00);
					}
					else
						c.setCellValue(ukupno[i].getCell(k).getStringCellValue());
				}
			}
		}
	}

	private static void upisiProizvode(Sheet sh, List<Proizvodi> lista, Row[] tabela) {
		int prviRed=29;
		int redniBroj=1;
		Row red=sh.createRow(prviRed);
		for(int i=0;i<10;i++){
			Cell celija=red.createCell(i);
			celija.setCellValue(tabela[0].getCell(i).getStringCellValue());
			celija.setCellStyle(tabela[0].getCell(i).getCellStyle());
		}
		prviRed++;//30
		for(int i=0;i<lista.size();i++){
			red=sh.createRow(prviRed+i);
			Proizvodi tren=lista.get(i);
			String[] upisati={(i+1)+"",tren.getSifra(), tren.getNaziv(),"kom",tren.getKolicina()+"",tren.getCijenaDb()+"",tren.getRabat()+"%",tren.getCijenaSaRabatomDb()+"",tren.getPdv()+"%",tren.getCijenaUkupnoDb()+""};
			for(int j=0;j<10;j++){
				System.out.print(upisati[j]+"\t");
				Cell cel=red.createCell(j);
				if(j==0 || (j>3 && j!=6 && j!=8)){
					cel.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cel.setCellValue(Double.parseDouble(upisati[j]));
				}
				else
					cel.setCellValue(upisati[j]);
				tabela[1].getCell(j).getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cel.setCellStyle(tabela[1].getCell(j).getCellStyle());
			}
			System.out.print("\n");
		}

	}

	public static void upisiKupca(Sheet sh, Kupac kupac){
		Row trenutni=sh.getRow(11);
		Cell upis=trenutni.getCell(1);
		upis.setCellValue(kupac.getNaziv());
		trenutni=sh.getRow(12);
		upis=trenutni.getCell(1);
		upis.setCellValue("Adresa: "+kupac.getAdresa());
		trenutni=sh.getRow(13);
		upis=trenutni.getCell(1);
		upis.setCellValue("Mesto: "+kupac.getMjesto());
		trenutni=sh.getRow(14);
		upis=trenutni.getCell(1);
		upis.setCellValue("PIB: "+kupac.getPib());
	}

	public static Row[] rezultatRedovi(Sheet s){
		Row[] redovi=new Row[5];
		for(int i=16;i<21;i++){
			redovi[i-16]=s.getRow(i);
		}
		return redovi;
	}

	public static Row zaglavlje(Sheet s){
		return s.getRow(22);
	}

	public static Row[] tabela(Sheet s){
		Row[] rezultat=new Row[2];
		rezultat[0]=s.getRow(24);
		for(int i=0;i<10;i++){
			System.out.println(rezultat[0].getCell(i).getStringCellValue());
		}
		rezultat[1]=s.getRow(25);
		return rezultat;
	}

}
