package com.example.productapp.domain.controller;

import com.example.productapp.domain.model.stock.dto.StockDto;
import com.example.productapp.domain.service.StockService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/stock")
@Api(tags = "Stock API")
@RequiredArgsConstructor
public class RestStockController {
    private final StockService stockService;

    @GetMapping("/report")
    public ResponseEntity<StockDto> report() {

        return new ResponseEntity<>(stockService.report(), HttpStatus.OK);

    }
}
