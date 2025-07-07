package com.techlab.api.dtos.order;

import com.techlab.api.constants.TokenConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemPayloadDTO {
    @NotNull(message = TokenConstants.PAYLOAD_ERROR, groups = {Create.class})
    @Positive(message = TokenConstants.PAYLOAD_POSITIVE_ERROR, groups = {Create.class})
    private Long productId;

    @NotNull(message = TokenConstants.PAYLOAD_ERROR, groups = {Create.class, Update.class})
    @Positive(message = TokenConstants.PAYLOAD_POSITIVE_ERROR, groups = {Create.class, Update.class})
    @Min(value = 1, message = TokenConstants.QUANTITY_MIN_ERROR, groups = {Create.class, Update.class})
    private Integer quantity;

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public interface Create {}

    public interface Update {}
}
