package exelProba;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import oprema.model.Proizvodi;
import oprema.model.ProizvodiServis;

public class MagacinPrinter {

	private ProizvodiServis ps;

	public MagacinPrinter(ProizvodiServis ps){
		this.ps=ps;
	}

	public void isprintajMagacine(String putanja){
		Workbook wb = new HSSFWorkbook();
		if(putanja.indexOf(".xls") < 0){
			putanja=putanja+".xls";
		}
		File fajl = new File(putanja);
		Sheet sh=wb.createSheet();
		Row r = sh.createRow(1);
		Cell celija=r.createCell(1);
		CellStyle stil = wb.createCellStyle();
		stil.setBorderBottom(CellStyle.BORDER_THICK);
		stil.setBorderLeft(CellStyle.BORDER_THICK);
		stil.setBorderRight(CellStyle.BORDER_THICK);
		stil.setBorderTop(CellStyle.BORDER_THICK);
		Font font=wb.createFont();
		font.setBold(true);
		stil.setFont(font);
		celija.setCellStyle(stil);
		celija.setCellValue("Šifra");
		celija = r.createCell(2);
		celija.setCellStyle(stil);
		celija.setCellValue("Naziv");
		celija=r.createCell(3);
		celija.setCellStyle(stil);
		celija.setCellValue("Količina");
		celija = r.createCell(0);
		celija.setCellStyle(stil);
		celija.setCellValue("Redni br.");
		int i =2;
		CellStyle stil2=wb.createCellStyle();
		Font font2 = wb.createFont();
		font2.setBold(false);
		stil2.setFont(font2);
		stil2.setBorderBottom(CellStyle.BORDER_THIN);
		stil2.setBorderLeft(CellStyle.BORDER_THIN);
		stil2.setBorderRight(CellStyle.BORDER_THIN);
		stil2.setBorderTop(CellStyle.BORDER_THIN);
		for(Proizvodi p : ps.sviProizvodi()){
			r = sh.createRow(i++);
			celija = r.createCell(0);
			celija.setCellStyle(stil2);
			celija.setCellValue(i-2);
			celija = r.createCell(1);
			celija.setCellStyle(stil2);
			celija.setCellValue(p.getSifra());
			celija = r.createCell(2);
			celija.setCellStyle(stil2);
			celija.setCellValue(p.getNaziv());
			celija = r.createCell(3);
			celija.setCellStyle(stil2);
			celija.setCellValue(p.getMaliStanje()+p.getVelikiStanje());
		}
		for(int j=0;j<4;j++)
			sh.autoSizeColumn(j);
		try {
			wb.write(new FileOutputStream(fajl));
			wb.close();
		} catch (IOException e) {
			return;
		}

		Thread td = new Thread(()->{
			try {
				Desktop.getDesktop().browse(fajl.getParentFile().toURI());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

}
