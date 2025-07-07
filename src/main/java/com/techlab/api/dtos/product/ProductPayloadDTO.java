package com.techlab.api.dtos.product;

import com.techlab.api.constants.TokenConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public class ProductPayloadDTO {
    @NotBlank(message = TokenConstants.PAYLOAD_ERROR, groups = Create.class)
    @Size(min = 4, max = 60, message = TokenConstants.PAYLOAD_LENGTH_ERROR, groups = {Create.class, Update.class})
    private String name;

    @NotBlank(message = TokenConstants.PAYLOAD_ERROR, groups = Create.class)
    @Size(min = 4, max = 255, message = TokenConstants.PAYLOAD_LENGTH_ERROR, groups = {Create.class, Update.class})
    private String description;

    @NotNull(message = TokenConstants.PAYLOAD_ERROR, groups = Create.class)
    @Positive(message = TokenConstants.PAYLOAD_POSITIVE_ERROR, groups = {Create.class, Update.class})
    private BigDecimal price;

    @NotBlank(message = TokenConstants.PAYLOAD_ERROR, groups = Create.class)
    @URL(message = TokenConstants.PAYLOAD_URL_ERROR, groups = {Create.class, Update.class})
    private String imageUrl;

    @NotNull(message = TokenConstants.PAYLOAD_ERROR, groups = Create.class)
    @Positive(message = TokenConstants.PAYLOAD_POSITIVE_ERROR, groups = {Create.class, Update.class})
    private Integer quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public interface Create {}

    public interface Update {}
}
