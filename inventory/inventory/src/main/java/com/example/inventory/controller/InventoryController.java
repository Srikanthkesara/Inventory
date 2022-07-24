package com.example.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.inventory.entity.Inventory;
import com.example.inventory.service.InventoryService;
import com.example.inventory.utility.CSVUtility;
import com.example.inventory.utility.ResponseMessage;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	@Autowired
	InventoryService inventoryService;
	@Autowired
	CSVUtility csvUtility;

	@PostMapping("/save")
	public ResponseEntity<ResponseMessage> saveInventoryData(@RequestParam("file") MultipartFile file) {
		String message = "";
		if (csvUtility.hasCSVFormat(file)) {
			try {
				inventoryService.saveInvetory(file);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	@GetMapping("/get?supplier={supplier} and filter={filter} and expiry={expiry}")
	public ResponseEntity<List<Inventory>> getInvetoryDataBySuppiler(@RequestParam String supplier,@RequestParam boolean filter,@RequestParam boolean expiry) {
		try {
			List<Inventory> result = inventoryService.getInvetoryDataBySuppiler(supplier,filter,expiry);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
	}

}
