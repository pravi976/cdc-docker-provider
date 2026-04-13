package com.fedex.cdc.demo.provider.inventory;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventorySeedConfig {
    @Bean
    ApplicationRunner seedInventory(InventoryRepository inventoryRepository) {
        return args -> {
            ensureExists(inventoryRepository, "SKU-ULTRA-001", 500, "MEM-01", "AVAILABLE");
            ensureExists(inventoryRepository, "SKU-COLD-CHAIN-777", 80, "AMS-02", "AVAILABLE");
        };
    }

    private void ensureExists(
            InventoryRepository repository,
            String sku,
            int qty,
            String warehouse,
            String status
    ) {
        repository.findById(sku).orElseGet(() -> {
            InventoryEntity entity = new InventoryEntity();
            entity.setSku(sku);
            entity.setAvailableQty(qty);
            entity.setWarehouseCode(warehouse);
            entity.setStatus(status);
            return repository.save(entity);
        });
    }
}

