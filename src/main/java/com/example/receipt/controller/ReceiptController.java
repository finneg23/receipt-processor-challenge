package com.example.receipt.controller;

import com.example.receipt.model.Receipt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ReceiptController {
@GetMapping("/testing")
    public String helloWorld() {
    return "hello, world!";
}

@PostMapping("/receipts/process")
    public String processReceipt(@Valid @RequestBody Receipt receipt) {
    return null;
}
}


/*
{
    "retailer": "Walgreens",
    "purchaseDate": "2022-01-02",
    "purchaseTime": "08:13",
    "total": "2.65",
    "items": [
        {"shortDescription": "Pepsi - 12-oz", "price": "1.25"},
        {"shortDescription": "Dasani", "price": "1.40"}
    ]
}
 */