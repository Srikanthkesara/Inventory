package com.example.inventory.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.utility.CSVUtility;

@Service
public class InventoryService {
	@Autowired
	InventoryRepository repository;
	@Autowired
	CSVUtility csvUtility;

	public void saveInvetory(MultipartFile file) {

		List<Inventory> inventory = csvUtility.getCsvRecords(file);
		repository.saveAll(inventory);

	}

	public List<Inventory> getInvetoryDataBySuppiler(String name, boolean filter, boolean expiry) {
		List<Inventory> result = repository.findAllBySuppiler(name);
		if (filter) {
			result = result.stream().sorted(Comparator.comparing(Inventory::getName)).collect(Collectors.toList());
		}
		if (expiry) {
			Date today = new Date();
			result = result.stream().filter(i -> i.getExp().before(today)).collect(Collectors.toList());
		}
		return result;
	}
	public Iterable<Inventory> getInvetoryDataWithSort(String field) {
		return repository.findAll(Sort.by(field));
	}
}
