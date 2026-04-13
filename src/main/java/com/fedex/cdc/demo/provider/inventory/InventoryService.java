package com.fedex.cdc.demo.provider.inventory;

import com.fedex.cdc.demo.provider.api.InventoryAvailabilityResponse;
import com.fedex.cdc.demo.provider.api.InventoryReservationRequest;
import com.fedex.cdc.demo.provider.api.InventoryReservationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryReservationRepository reservationRepository;

    public InventoryService(InventoryRepository inventoryRepository, InventoryReservationRepository reservationRepository) {
        this.inventoryRepository = inventoryRepository;
        this.reservationRepository = reservationRepository;
    }

    public InventoryAvailabilityResponse availability(String sku) {
        InventoryEntity entity = inventoryRepository.findById(sku)
                .orElseGet(() -> defaultInventory(sku));
        return new InventoryAvailabilityResponse(
                entity.getSku(),
                entity.getAvailableQty(),
                entity.getWarehouseCode(),
                entity.getStatus()
        );
    }

    @Transactional
    public InventoryReservationResponse reserve(InventoryReservationRequest request) {
        InventoryEntity inventory = inventoryRepository.findById(request.sku())
                .orElseGet(() -> defaultInventory(request.sku()));
        int reserved = Math.min(inventory.getAvailableQty(), request.requestedQty());
        inventory.setAvailableQty(inventory.getAvailableQty() - reserved);
        inventoryRepository.save(inventory);

        InventoryReservationEntity reservation = new InventoryReservationEntity();
        reservation.setReservationId(request.reservationId());
        reservation.setOrderId(request.orderId());
        reservation.setSku(request.sku());
        reservation.setQty(reserved);
        reservation.setStatus(reserved == request.requestedQty() ? "RESERVED" : "PARTIAL");
        reservationRepository.save(reservation);

        return new InventoryReservationResponse(
                reservation.getReservationId(),
                reservation.getSku(),
                reservation.getQty(),
                reservation.getStatus(),
                reserved == request.requestedQty() ? "NONE" : "LOW_STOCK"
        );
    }

    private InventoryEntity defaultInventory(String sku) {
        InventoryEntity entity = new InventoryEntity();
        entity.setSku(sku);
        entity.setAvailableQty(250);
        entity.setWarehouseCode("MEM-01");
        entity.setStatus("AVAILABLE");
        return inventoryRepository.save(entity);
    }
}

