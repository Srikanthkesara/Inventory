package com.example.inventory.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.example.inventory.entity.Inventory;

public class CSVUtility {
	public static String TYPE = "text/csv";

	public boolean hasCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public List<Inventory> getCsvRecords(MultipartFile file){
		InputStream is=null;
		try {
			is = file.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			CSVParser csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			List<Inventory> inventories = new ArrayList<>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
			for (CSVRecord csvRecord : csvRecords) {
				try {
					Inventory inventory = new Inventory(
							csvRecord.get("code"),
							csvRecord.get("name"),
							csvRecord.get("batch"),
							Integer.parseInt(csvRecord.get("stock")),
							Integer.parseInt(csvRecord.get("deal")),
							Integer.parseInt(csvRecord.get("free")),
							Double.parseDouble(csvRecord.get("mrp")),
							Double.parseDouble(csvRecord.get("rate")),
							formatter.parse(csvRecord.get("exp")),
							csvRecord.get("company"),
							csvRecord.get("supplier")
							);
					inventories.add(inventory);
				} catch (Exception e) {
				}
				
			}
			return inventories;

		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

}
