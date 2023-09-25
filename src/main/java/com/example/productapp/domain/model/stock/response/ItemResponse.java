package com.example.productapp.domain.model.stock.response;

import com.example.productapp.domain.model.stock.dto.ItemDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Setter
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse {

    @JsonProperty("item")
    private ItemDto itemDto;

    private String message;
}
