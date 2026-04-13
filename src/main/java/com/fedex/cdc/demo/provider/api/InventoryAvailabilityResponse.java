package com.fedex.cdc.demo.provider.api;

public record InventoryAvailabilityResponse(
        String sku,
        int availableQty,
        String warehouseCode,
        String status
) {
}

