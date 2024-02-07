package com.inventory.consumer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.consumer.entity.InventoryEvent;

@Repository
public interface InventoryEventRepo extends JpaRepository<InventoryEvent, String> {
}
