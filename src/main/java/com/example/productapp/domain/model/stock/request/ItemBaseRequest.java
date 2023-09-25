package com.example.productapp.domain.model.stock.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ItemBaseRequest {

    @NotBlank
    @Schema(
            name = "Name of the item",
            example = ""
    )
    private String name;

    @Schema(
            name = "Value indicating whether the item has been sold or not",
            example = "",
            defaultValue = "false"
    )
    private Boolean isSold = Boolean.FALSE;
}
