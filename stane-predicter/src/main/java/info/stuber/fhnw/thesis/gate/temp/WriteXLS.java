package info.stuber.fhnw.thesis.gate.temp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteXLS {
	
	public static void csvWriter(int totalFiles, ArrayList<ArrayList<String>> rows) 
			throws IOException{
		
		System.out.println("Writing output file..");
		long csvStartTime = System.currentTimeMillis();
				
		FileOutputStream xlsFileStream = new FileOutputStream(new File("src/output/output11043/output.xlsx"));

		// create a workbook and a sheet
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Outputsheet");		
		
		for(int x=0; x<=totalFiles; x++){
			Row row = sheet.createRow(x);
			for(int y=0;y<rows.size();y++){
				// create a cell
				Cell cell = row.createCell(y);
				if(x<rows.get(y).size()){				
					// put value in cell
					cell.setCellValue(rows.get(y).get(x));
				}
				else {
					// no element so put empty value
					cell.setCellValue(" ");
				}
			}
		}
		
		// write sheet in file
		workbook.write(xlsFileStream);
		xlsFileStream.close();
		
		long csvEndTime = System.currentTimeMillis();
		long csvTime = csvEndTime-csvStartTime;
		System.out.println("Time to write all files: "+csvTime+"ms");
	}
}
