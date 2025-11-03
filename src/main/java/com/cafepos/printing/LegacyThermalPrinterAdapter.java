package com.cafepos.printing;

import vendor.legacy.LegacyThermalPrinter;

public final class LegacyThermalPrinterAdapter implements Printer {
    private final LegacyThermalPrinter legacy;

    public LegacyThermalPrinterAdapter(LegacyThermalPrinter legacy) {
        this.legacy = legacy;
        this.legacy.init();
    }

    @Override
    public void print(String receipt) {
        if (receipt == null) return;
        for (String line : receipt.split("\n")) {
            legacy.printLine(line);
        }
        legacy.cut();
    }
}


