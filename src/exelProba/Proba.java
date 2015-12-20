package exelProba;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.record.aggregates.MergedCellsTable;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import oprema.model.Kupac;
import oprema.model.Obracun;
import oprema.model.Proizvodi;
import oprema.model.ProizvodiServis;

public class Proba {

	private List<Proizvodi> lista;
	private Kupac kupac;
	private Obracun obracun;
	private File fajl;


	public static void main(String[] args) throws IOException{
		ProizvodiServis ps=new ProizvodiServis();
		List<Proizvodi> proizvodi=ps.sviProizvodi();
		Obracun ob=new Obracun();
		for(Proizvodi a : proizvodi){
			a.postaviSve();
			ob.stavi(a);
		}
		Kupac k=new Kupac();
		k.setNaziv("Naziv 1");
		k.setAdresa("Adresa 2");
		k.setMjesto("Mjesto 3");
		k.setPib("1234568");
		File f=new File("proba 33.xls");
		Proba p=new Proba(proizvodi, k, ob, f);
		p.generisi();
		ps.zatvoriKlasu();
	}
	public Proba(List<Proizvodi> l, Kupac k, Obracun o, File f){
		lista=l;
		kupac=k;
		obracun=o;
		fajl=f;
	}

	public void generisi() throws IOException {

		InputStream is=Proba.class.getClassLoader().getResourceAsStream("racun 01-M-2015 boza 2.xls");
		Workbook wb=new HSSFWorkbook(is);
		Sheet sh=wb.getSheetAt(0);
		Row[] ukupno=rezultatRedovi(sh);
		Row zaglavlje=zaglavlje(sh);
		Row[] tabela=tabela(sh);

		for(int k=0;k<sh.getMergedRegions().size();k++){
			org.apache.poi.ss.util.CellRangeAddress ca=sh.getMergedRegion(k);
			if(ca.isInRange(16, 7) || ca.isInRange(17, 7) || ca.isInRange(18, 7) || ca.isInRange(20, 7) || ca.isInRange(22, 7)){
				ca.setLastColumn(6);
			}
		}
		upisiKupca(sh, kupac);

		upisiProizvode(sh, lista, tabela);

		upisiCijene(sh,lista, ukupno);

		upisiZaglavlje(zaglavlje, sh);

		for(int j=16;j<29;j++){
			Row red=sh.getRow(j);
			if(red!=null)
				for(int k=0;k<11;k++){
					Cell celija=red.getCell(k);
					if(celija!=null)
						red.removeCell(celija);
				}
		}

		sh.shiftRows(29, sh.getLastRowNum(), -13);

		for(int i=0;i<11;i++){
			sh.autoSizeColumn(i);
		}
		FileOutputStream fs=new FileOutputStream(fajl);
		wb.write(fs);
		fs.flush();
		fs.close();
		is.close();
		wb.close();
	}

	private void upisiZaglavlje(Row zaglavlje, Sheet sh) {
		int prviRed=30+lista.size()+18;
		Row tren=sh.createRow(prviRed);
		for(int i=0;i<zaglavlje.getLastCellNum();i++){
			Cell celijaFrom=zaglavlje.getCell(i);
			if(celijaFrom!=null){
				Cell celijaTo=tren.createCell(i);
				celijaTo.setCellStyle(celijaFrom.getCellStyle());
				celijaTo.setCellValue(celijaFrom.getStringCellValue());
			}
		}

	}
	private void upisiCijene(Sheet sh, List<Proizvodi> lista, Row[] ukupno) {
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
						if(i==0)
							c.setCellValue(obracun.getGlavnicaUDb());
						else if(i==1)
							c.setCellValue(obracun.getPdvUDb());
						else if(i==4)
							c.setCellValue(obracun.getUkupnoSve());
					}
					else
						c.setCellValue(ukupno[i].getCell(k).getStringCellValue());
				}
			}
		}
	}

	private void upisiProizvode(Sheet sh, List<Proizvodi> lista, Row[] tabela) {
		int prviRed=29;
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

	public void upisiKupca(Sheet sh, Kupac kupac){
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

	public Row[] rezultatRedovi(Sheet s){
		Row[] redovi=new Row[5];
		for(int i=16;i<21;i++){
			redovi[i-16]=s.getRow(i);
		}
		return redovi;
	}

	public Row zaglavlje(Sheet s){
		return s.getRow(22);
	}

	public Row[] tabela(Sheet s){
		Row[] rezultat=new Row[2];
		rezultat[0]=s.getRow(24);
		for(int i=0;i<10;i++){
			System.out.println(rezultat[0].getCell(i).getStringCellValue());
		}
		rezultat[1]=s.getRow(25);
		return rezultat;
	}

}
