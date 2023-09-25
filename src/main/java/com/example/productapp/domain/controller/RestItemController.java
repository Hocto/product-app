package com.example.productapp.domain.controller;

import com.example.productapp.domain.mapper.ItemMapper;
import com.example.productapp.domain.model.stock.entity.CompositeKey;
import com.example.productapp.domain.model.stock.request.ItemBaseRequest;
import com.example.productapp.domain.model.stock.request.ItemRequest;
import com.example.productapp.domain.model.stock.response.ItemResponse;
import com.example.productapp.domain.service.ItemService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
@Api(tags = "Item API")
@Validated
public class RestItemController {

    private final ItemMapper itemMapper;
    private final ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<ItemResponse> retrieveItem(@Valid @RequestBody ItemBaseRequest itemRequest) {

        return new ResponseEntity<>(ItemResponse.builder()
                .itemDto(itemService.retrieve(CompositeKey.builder()
                        .name(itemRequest.getName())
                        .isSold(itemRequest.getIsSold())
                        .build()))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/item")
    public ResponseEntity<ItemResponse> addItem(@Valid @RequestBody ItemRequest itemRequest) {

        var savedDto = itemService.add(itemMapper.parse(itemRequest));
        if (savedDto == null) {
            return new ResponseEntity<>(ItemResponse.builder().message("Item is already exist").build(), HttpStatus.OK);
        }

        return new ResponseEntity<>(ItemResponse.builder().itemDto(savedDto).build(), HttpStatus.OK);
    }

    @PutMapping("/item")
    public ResponseEntity<ItemResponse> updateItem(@Valid @RequestBody ItemRequest request) {

        return new ResponseEntity<>(ItemResponse.builder()
                .itemDto(itemService.modify(itemMapper.parse(request))).build(), HttpStatus.OK);
    }

    @DeleteMapping("/item")
    public ResponseEntity<ItemResponse> deleteItem(@Valid @RequestBody ItemBaseRequest itemRequest) {

        itemService.remove(CompositeKey.builder().name(itemRequest.getName()).isSold(itemRequest.getIsSold()).build());
        return new ResponseEntity<>(ItemResponse.builder().message("Item was deleted successfully.").build(), HttpStatus.OK);
    }

    @PostMapping("/item/sell")
    public ResponseEntity<ItemResponse> sellItem(
            @NotBlank @RequestParam("name") String name,
            @Min(0) @RequestParam("quantity") int quantity
    ) {

        itemService.sellItem(name, quantity);

        return new ResponseEntity<>(ItemResponse.builder()
                .message(String.format("%s piece/pieces of item were sold successfully.", quantity))
                .build(), HttpStatus.OK);
    }
}
