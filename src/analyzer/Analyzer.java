package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xwpf.extractor.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

public class Analyzer {
	private ArrayList<Theme> themes;
        private ArrayList<File> files;
        private int lineCount;
	
	public Analyzer(){
		themes = new ArrayList<Theme>();
                files=new ArrayList<File>();
                lineCount=0;
	}
	public Analyzer(ArrayList<Theme> newThemes, ArrayList<File> newFiles){
		themes=newThemes;
                files=newFiles;
                lineCount=0;
	}
	public ArrayList<Theme> getThemes(){
		return themes;
	}
        public int getLineCount(){
            return lineCount;
        }
	public void addTheme(Theme t){
		themes.add(t);
	}
        public void addTheme(String name){
            themes.add(new Theme(name));
        }
        public void setThemes(ArrayList<Theme> toSet){
            this.themes=toSet;
        }
        public ArrayList<File> getFiles(){
            return files;
        }
        //It is preferred that absolute paths with Unix conventions be used
        public void addFile(File fileName){
            files.add(fileName);
        }
	public void analyze(String docName) throws FileNotFoundException, IOException{
            for(int i=0;i<files.size();i++){
                File f = files.get(i);
                if(f.getName().contains(".txt"))
                    analyzeTextFile(f);
                else if(f.getName().contains(".docx"))
                    analyzeWordFile(f);
            }
            generateWordDoc(docName+".docx");
            generateExcelDoc(docName+".xlsx");
            
	}
        private void analyzeTextFile(File fileName) throws FileNotFoundException{
            Scanner inStream=new Scanner(fileName);
		int location =0;
		while(inStream.hasNextLine()){
			String line = inStream.nextLine();
			checkForKeywords(fileName.getName(),line,location);
			location++;
		}
		inStream.close();
        }
        private void analyzeWordFile(File fileName) throws IOException{
            XWPFDocument doc = new XWPFDocument(new FileInputStream(fileName));
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            String[] docLines = extractor.getText().split("\n");
            for(int i=0;i<docLines.length;i++){
                checkForKeywords(fileName.getName(),docLines[i],i);
            }
        }
	//There has to be a more efficient way to do this
	//Perhaps hashtables
	private void checkForKeywords(String fileName, String line, int loc){
		for(Theme t: themes){
			for(Keyword k: t.getKeywords()){
				if(line.contains(k.getName()))
					k.addOccurrence(fileName, line, loc);
			}
		}
                lineCount++;
        }
        private void generateWordDoc(String docName) throws FileNotFoundException, IOException{
            XWPFDocument doc = new XWPFDocument();
            for(Theme t: themes){
                for(Keyword k: t.getKeywords()){
                    for(Occurrence c: k.getOccurrs()){
                        XWPFParagraph p = doc.createParagraph();
                        p.setAlignment(ParagraphAlignment.LEFT);
                        XWPFRun r = p.createRun();
                        setRunAttributes(r);
                        r.setText(c.getOccurInfo());
                        r.addCarriageReturn();
                        
                        String[] strings = c.getSentece().split(k.getName());
                        for(int i=0; i<strings.length;i++){
                        XWPFRun r2 = p.createRun();
                        setRunAttributes(r2);
                        r2.setText(strings[i]);
                        
                        if(i<strings.length-1){
                            XWPFRun r3 = p.createRun();
                            setRunAttributes(r3);
                            r3.setBold(true);
                            r3.setItalic(true);
                            r3.setColor(t.getHexColor());
                            r3.setText(k.getName());
                        }
                        
                        }
                    }
                }
            }
            FileOutputStream outStream = new FileOutputStream(docName);
            doc.write(outStream);
            outStream.close();
        }
        private void generateExcelDoc(String docName) throws FileNotFoundException, IOException{
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet overall = workbook.createSheet("Overall");
            XSSFRow row = overall.createRow(0);
            
            XSSFCellStyle topStyle = workbook.createCellStyle();
            topStyle.setAlignment(CellStyle.ALIGN_CENTER);
            
            XSSFCell theme = row.createCell(0);
            theme.setCellValue("Theme");
            overall.autoSizeColumn(0);
            
            XSSFCell occurs = row.createCell(1);
            occurs.setCellValue("Occurrences");
            overall.autoSizeColumn(1);
            
            XSSFCell prev = row.createCell(2);
            prev.setCellValue("Prevalence");
            overall.autoSizeColumn(2);
            
            theme.setCellStyle(topStyle);
            occurs.setCellStyle(topStyle);
            prev.setCellStyle(topStyle);
            
            for(int i=0; i<themes.size(); i++){
                XSSFRow r = overall.createRow((i+1));
                
                XSSFCell c = r.createCell(0);
                c.setCellValue(themes.get(i).getName());
                
                XSSFCell c1 = r.createCell(1);
                c1.setCellValue(themes.get(i).getTotalOccurs());
                
                XSSFCell c2 = r.createCell(2);
                c2.setCellValue(calculatePrevalence(themes.get(i).getTotalOccurs(),lineCount));
            }
            
            //This could be done in the previous loop but since we don't need 
            //indices as much, we may as well use the cleaner for each loop
            
            for(Theme t: themes){
                XSSFSheet themeSheet = workbook.createSheet(t.getName());
                XSSFRow row1 = themeSheet.createRow(0);
                
                XSSFCell keyword = row1.createCell(0);
                keyword.setCellValue("Keyword");
                keyword.setCellStyle(topStyle);
                
                XSSFCell occ = row1.createCell(1);
                occ.setCellValue("Occurrences");
                occ.setCellStyle(topStyle);
                
                XSSFCell themePrev = row1.createCell(2);
                themePrev.setCellValue("Prevalence");
                themePrev.setCellStyle(topStyle);
                
                for(int i=0; i<t.getKeywords().size();i++){
                    Keyword k = t.getKeywords().get(i);
                    XSSFRow r = themeSheet.createRow((i+1));
                
                    XSSFCell c = r.createCell(0);
                    c.setCellValue(k.getName());
                
                    XSSFCell c1 = r.createCell(1);
                    c1.setCellValue(k.getNumOccurs());
                
                    XSSFCell c2 = r.createCell(2);
                    c2.setCellValue(calculatePrevalence(k.getNumOccurs(),t.getTotalOccurs()));
                }
            }
            
            FileOutputStream output = new FileOutputStream(docName);
            workbook.write(output);
            output.close();
        }
        private void setRunAttributes(XWPFRun run){
            run.setFontFamily("Calibri");
            run.setFontSize(12);
        }
        private double calculatePrevalence(int occurs, int count){
         return (double)occurs/(double)count;
        }
}
