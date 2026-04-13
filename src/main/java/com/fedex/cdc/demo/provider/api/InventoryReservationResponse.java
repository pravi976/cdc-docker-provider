package com.fedex.cdc.demo.provider.api;

public record InventoryReservationResponse(
        String reservationId,
        String sku,
        int reservedQty,
        String reservationStatus,
        String reasonCode
) {
}

