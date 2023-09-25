package com.example.productapp.domain.model.stock.entity;


import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @EmbeddedId
    @NotNull
    private CompositeKey compositeKey;

    private BigDecimal price;

    private int quantity;
}
