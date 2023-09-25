package com.example.productapp.domain.model.stock.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class CompositeKey implements Serializable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isSold;
}
