package com.example.productapp.domain.model.stock.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ItemRequest extends ItemBaseRequest {

    @NotNull
    @DecimalMin("0.0")
    @Schema(
            name = "Price of the item",
            example = "7.25"
    )
    private BigDecimal price;

    @Min(0)
    @Schema(
            name = "Quantity of the item",
            example = "1"
    )
    private int quantity;
}
