package com.yuhe.mywebmagic.ocr;
import java.io.File;  
import java.io.IOException;  
  
public class TestOCR {  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        String path = "d:\\code.jpg";     
        try {     
            String valCode = new OCR().recognizeText(new File(path), "jpg", null);     
            System.out.println(valCode);     
        } catch (IOException e) {     
            e.printStackTrace();     
        } catch (Exception e) {  
            e.printStackTrace();  
        }      
    }  
  
}  