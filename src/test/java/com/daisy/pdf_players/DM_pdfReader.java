/**
 * 
 */
package com.daisy.pdf_players;

/**
 * 
 */

	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.Scanner;
	import org.apache.pdfbox.Loader;
	import org.apache.pdfbox.pdmodel.PDDocument;
	import org.apache.pdfbox.text.PDFTextStripper;
	import org.json.JSONArray;
	import org.json.JSONObject;

	public class DM_pdfReader {

	    // Method to read the PDF file and extract text page-wise in JSON format
	    public JSONObject readPDFFile(String fileLocation) {
	        File file = new File(fileLocation);
	        PDDocument doc = null;
	        JSONObject pdfData = new JSONObject();
	        JSONArray pagesArray = new JSONArray();

	        try (FileInputStream fis = new FileInputStream(file)) {
	        	doc = Loader.loadPDF(file);
	            PDFTextStripper pdfStripper = new PDFTextStripper();
	            int numberOfPages = doc.getNumberOfPages();
	            
	            // Loop through each page and extract text
	            for (int i = 1; i <= numberOfPages; i++) {
	                pdfStripper.setStartPage(i);
	                pdfStripper.setEndPage(i);
	                String pageText = pdfStripper.getText(doc);

	                // Create a JSON object for each page
	                JSONObject pageObject = new JSONObject();
	                pageObject.put("pageNumber", i);
	                pageObject.put("text", pageText.trim());
	                pagesArray.put(pageObject);
	            }

	            // Add the array of pages to the main JSON object
	            pdfData.put("pages", pagesArray);
	            pdfData.put("status", "success");
	            pdfData.put("message", "Data extracted successfully.");

	        } catch (IOException e) {
	            pdfData.put("status", "error");
	            pdfData.put("message", "An error occurred while reading the PDF: " + e.getMessage());
	            e.printStackTrace(); // Print stack trace for debugging
	        } finally {
	            // Ensure the document is closed if it was opened
	            if (doc != null) {
	                try {
	                    doc.close();
	                } catch (IOException e) {
	                    System.err.println("An error occurred while closing the document: " + e.getMessage());
	                }
	            }
	        }

	        return pdfData; // Return the JSON object containing all page data
	    }
	    	    
	    
	 // Method to save JSON data to a file with a timestamp
	    public void saveJsonToFile(JSONObject jsonData) {
	        String timestamp = String.valueOf(System.currentTimeMillis()); // Get current timestamp
	        String fileName = "Daisy Generated Data on " + timestamp + ".json"; // Create filename
	        try (FileWriter fileWriter = new FileWriter(fileName)) {
	            fileWriter.write(jsonData.toString(4)); // Write JSON data with pretty-print
	            System.out.println("SJTF File has been stored at location: " + new File(fileName).getAbsolutePath());
	        } catch (IOException e) {
	            System.err.println("An error occurred while saving the JSON file: " + e.getMessage());
	        }
	    }
	    
	 // Method to write JSON data to a file 
	    public void writeJsonToFile(JSONObject jsonData, String defaultFilePath, String customPath) {
	        // Determine the path to use
	        String pathToUse = (customPath == null || customPath.isEmpty() || customPath.isBlank()) ? defaultFilePath : customPath;
	        FileWriter fileWriter = null;

	        try {
	            // Create a File object with the determined path
	            File file = new File(pathToUse);

	            // Create directories if they do not exist
	            File parentDir = file.getParentFile();
	            if (parentDir != null && !parentDir.exists()) {
	                boolean dirCreated = parentDir.mkdirs();
	                if (!dirCreated) {
	                    System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
	                    return; // Exit if directory creation fails
	                }
	            }

	            // Initialize FileWriter
	            fileWriter = new FileWriter(file);
	            
	            // Write JSON data to the file
	            fileWriter.write(jsonData.toString(4)); // Pretty-print with 4 spaces indentation
	            System.out.println("WJJTF File has been stored at location: " + file.getAbsolutePath());

	        } catch (IOException e) {
	            System.err.println("An error occurred while writing to the file: " + e.getMessage());
	        } finally {
	            // Ensure the FileWriter is closed
	            if (fileWriter != null) {
	                try {
	                    fileWriter.close();
	                } catch (IOException e) {
	                    System.err.println("An error occurred while closing the file writer: " + e.getMessage());
	                }
	            }
	        }
	    }
	    public static void main(String[] args) {
	    	DM_pdfReader ob = new DM_pdfReader();
	    	long startTime = System.currentTimeMillis();
	        // Specify your PDF file path here
	        String fileName ;
//	        , defaultGeneratedFilepath ,createdFile
	        Scanner sc = new Scanner(System.in);
//	        System.out.println("Enter Input File path");
//	        fileName =  sc.nextLine();
//	        System.out.println("File will be generated at " + fileName);
	        fileName = "C:\\Users\\Dsnip\\Downloads\\spacepdfData.pdf";
//	        defaultGeneratedFilepath = "D:\\Coding\\Ecllipse\\Ecllipse Projects\\pdf_players\\Generated File";
//	        createdFile = "D:\\Coding\\Ecllipse\\Ecllipse Projects\\pdf_players\\Generated File";

	        // Call the method to read the PDF file and get JSON data
	        JSONObject jsonData = ob.readPDFFile(fileName);
	        // Print the JSON data
	     // Save JSON data to file
	        ob.saveJsonToFile(jsonData);
//	        ob.writeJsonToFile(jsonData, defaultGeneratedFilepath,createdFile);
	        sc.close();
	        System.out.println(jsonData.toString(4));
	        // Pretty-print with 4 spaces indentation
	        long endTime = System.currentTimeMillis();
	        long duration = endTime - startTime;
	        long seconds = (duration / 1000) % 60;
	        long minutes = (duration / (1000 * 60)) % 60;
	        long hours = (duration / (1000 * 60 * 60));
	        System.out.println("Total Time take to Read and give output is "+ hours+" hours: "+minutes+" minutes: "+seconds+" seconds");
	        
	        
	    }
	}
