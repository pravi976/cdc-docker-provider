package com.fedex.cdc.demo.provider.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record InventoryReservationRequest(
        @NotBlank String reservationId,
        @NotBlank String sku,
        @Min(1) int requestedQty,
        @NotBlank String orderId,
        @NotBlank String shipNode,
        @NotBlank String priority
) {
}

