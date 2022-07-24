package com.example.inventory.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Inventory;

@Repository
public interface InventoryRepository extends PagingAndSortingRepository<Inventory,Integer> {

	List<Inventory> findAllBySuppiler(String name);
}
