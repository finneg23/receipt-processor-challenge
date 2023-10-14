package com.example.receipt.services;

import com.example.receipt.model.Item;
import com.example.receipt.model.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class ReceiptServices {
    private Map<String, Receipt> receipts;
    private Map<String, Integer> points;
    @Autowired
    public ReceiptServices(Map<String, Receipt> receipts, Map<String, Integer> points) {
        this.receipts = receipts;
        this.points = points;
    }

    public String processReceipt(Receipt receipt) {
        receipt.setId(UUID.randomUUID().toString());
        receipts.put(receipt.getId(), receipt);
        points.put(receipt.getId(), calculatePoints(receipt));
        return receipt.getId();
    }

    public int getPoints(String id) {
        return points.get(id);
    }

    public Map<String, Receipt> getAllReceipts() {
        return receipts;
    }

    public int calculatePoints(Receipt receipt) {
        //start with 0 pts allotted
        int pts = 0;
        //get rid of all the spaces in the retailer name
        String retailerSplit = receipt.getRetailer().replace(" ", "");
        //add the length to the pts
        pts += retailerSplit.length();
        //initialize a BD version of the string total
        BigDecimal total = new BigDecimal(receipt.getTotal()).setScale(2, RoundingMode.HALF_UP);
        //if the number is whole, give 50 pts, if it could be divided by a quarter, give 25 pts, else give 0 pts
        if (total.remainder(BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP)).equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))) {
            pts += 50;
        } else if (total.remainder(BigDecimal.valueOf(0.25).setScale(2, RoundingMode.HALF_UP)).equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))) {
            pts += 25;
        }
        //check to see if the list is even big enough to consider
        if (receipt.getItems().size() > 1) {
        //if the list is an even length, take the length, divide by two, and multiply by 5
            if (receipt.getItems().size() % 2 == 1) {
                pts += ((receipt.getItems().size() - 1) / 2) * 5;
        //if not, take the length, minus one, divide by two, multiply by 5
            } else {pts += (receipt.getItems().size() / 2) * 5;}
        }
        //iterate through the list of items on the receipt
        for (Item item : receipt.getItems()) {
        //trim the extra spaces off the item desc. and if the length is evenly divisible by 3...
            if (item.getShortDescription().trim().length() % 3 == 0) {
        //turn the item price into a BD then multiply by 0.2 then round up to the nearest int
                BigDecimal itemPts = (new BigDecimal(item.getPrice()).multiply(BigDecimal.valueOf(0.2)));
                itemPts = itemPts.setScale(0, RoundingMode.CEILING);
        //turn that back into an int and add it in
                pts += itemPts.intValue();
            }
        }
        //parse an int out of the end of the purchase date and see if it is even, then add 6 pts
        if (Integer.parseInt(receipt.getPurchaseDate().substring((receipt.getPurchaseDate().length() - 2))) % 2 == 1) {
            pts += 6;
        }
        //parse an int out of the beginning of the purchase time (only works with 24hr) and see if it is between 14 and 16, then add 10pts
        if (Integer.parseInt(receipt.getPurchaseTime().substring(0, 2)) > 14 && Integer.parseInt(receipt.getPurchaseTime().substring(0, 2)) < 16) {
            pts += 10;
        }

        return pts;
    }
}
