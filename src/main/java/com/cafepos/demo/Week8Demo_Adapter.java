package com.cafepos.demo;

import com.cafepos.checkout.ReceiptPrinter;
import com.cafepos.common.Money;
import com.cafepos.printing.LegacyThermalPrinterAdapter;
import com.cafepos.printing.Printer;

import vendor.legacy.LegacyThermalPrinter;

public final class Week8Demo_Adapter {
    public static void main(String[] args) {
        System.out.println("=== Week 8 Demo: Adapter ===");
        ReceiptPrinter printer = new ReceiptPrinter(false);
        String receipt = printer.printReceipt("LAT+L", 2,
                Money.of(7.80), Money.of(0.39), Money.of(0.74), Money.of(8.15));

        Printer legacyAdapter = new LegacyThermalPrinterAdapter(new LegacyThermalPrinter());
        legacyAdapter.print(receipt);
    }
}


