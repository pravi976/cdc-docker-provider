package com.fedex.cdc.demo.provider.api;

import com.fedex.cdc.demo.provider.inventory.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{sku}")
    public InventoryAvailabilityResponse availability(@PathVariable("sku") String sku) {
        return inventoryService.availability(sku);
    }

    @PostMapping("/reservations")
    public InventoryReservationResponse reserve(@Valid @RequestBody InventoryReservationRequest request) {
        return inventoryService.reserve(request);
    }
}

