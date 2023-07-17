package com.project.chatbot;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.pipeline.Annotation;
import vn.pipeline.Sentence;
import vn.pipeline.VnCoreNLP;
import vn.pipeline.Word;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class VnCoreNLPExample {
    public static void main(String[] args) throws IOException {
    
        // "wseg", "pos", "ner", and "parse" refer to as word segmentation, POS tagging, NER and dependency parsing, respectively. 
        String[] annotators = {"wseg", "pos", "ner", "parse"}; 
        VnCoreNLP pipeline = new VnCoreNLP(annotators); 

        Data data = new Data();
        String str = data.getData();
        Annotation annotation = new Annotation(str); 
        pipeline.annotate(annotation); 
        List<Word> words = annotation.getWords();
        List<Sentence> sentences = annotation.getSentences();
        List<String> tokens = annotation.getTokens();
        String wordSegmentedText = annotation.getWordSegmentedText();
        String wordSegmentedTaggedText = annotation.getWordSegmentedTaggedText();
        System.out.println(annotation.toString());
        System.out.println(words.toString());

        // Tạo một workbook mới
        Workbook workbook = new XSSFWorkbook();
        // Tạo một trang tính mới trong workbook
        Sheet sheet = workbook.createSheet("Words");

        // Tạo tiêu đề cho các cột
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Index");
        headerRow.createCell(1).setCellValue("Form");
        headerRow.createCell(2).setCellValue("POS Tag");
        headerRow.createCell(3).setCellValue("NER Label");
        headerRow.createCell(4).setCellValue("Head");
        headerRow.createCell(5).setCellValue("Dep Label");
        headerRow.createCell(6).setCellValue("Entity");
        headerRow.createCell(7).setCellValue("Intent");

        // Ghi dữ liệu từ danh sách words vào các hàng trong sheet
        int rowIndex = 1;
        for (Sentence sentence : sentences) {
            for (Word word : sentence.getWords()) {
                Row dataRow = sheet.createRow(rowIndex);
                dataRow.createCell(0).setCellValue(word.getIndex());
                dataRow.createCell(1).setCellValue(word.getForm());
                dataRow.createCell(2).setCellValue(word.getPosTag());
                dataRow.createCell(3).setCellValue(word.getNerLabel());
                dataRow.createCell(4).setCellValue(word.getHead());
                dataRow.createCell(5).setCellValue(word.getDepLabel());

                if (word.getDepLabel().equals("pob") && !word.getPosTag().equals("P")) {
                    dataRow.createCell(6).setCellValue(word.getDepLabel());
                }

                rowIndex++;
            }
            rowIndex++;
        }


        // Lưu workbook vào file
        try (FileOutputStream fileOut = new FileOutputStream("output.xlsx")) {
            workbook.write(fileOut);
            System.out.println("File Excel đã được tạo thành công!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Đóng workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 1    Ông                 Nc  O       4   sub 
        // 2    Nguyễn_Khắc_Chúc    Np  B-PER   1   nmod
        // 3    đang                R   O       4   adv
        // 4    làm_việc            V   O       0   root
        // ...
        
        //Write to file
//        PrintStream outputPrinter = new PrintStream("output.txt");
//        pipeline.printToFile(annotation, outputPrinter);
    
        // You can also get a single sentence to analyze individually 
//        Sentence firstSentence = annotation.getSentences().get(0);
//        System.out.println(firstSentence.toString());
    }
}