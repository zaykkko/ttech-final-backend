package com.techlab.api.dtos.category;

import com.techlab.api.constants.TokenConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryPayloadDTO {
    @NotBlank(message = TokenConstants.PAYLOAD_ERROR, groups = Create.class)
    @Size(min = 4, max = 60, message = TokenConstants.PAYLOAD_LENGTH_ERROR, groups = {Create.class, Update.class})
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface Create {}
    public interface Update {}
}
