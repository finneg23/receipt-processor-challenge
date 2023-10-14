package com.example.receipt.controller;

import com.example.receipt.model.Receipt;
import com.example.receipt.services.ReceiptServices;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ReceiptController {
    @Autowired
    private ReceiptServices receiptServices;
@GetMapping("/receipts")
    public Map<String, Receipt> getAllReceipts() {
    return receiptServices.getAllReceipts();
}

@ResponseStatus(HttpStatus.CREATED)
@PostMapping("/receipts/process")
    public String processReceipt(@Valid @RequestBody Receipt receipt) {
   return receiptServices.processReceipt(receipt);
}

@GetMapping("/receipts/{id}/points")
    public int getPoints(@Valid @PathVariable String id) {
    try {
        return receiptServices.getPoints(id);
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This receipt could not be found");
    }
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