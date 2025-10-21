package com.cafepos.checkout;

import com.cafepos.common.Money;

public final class ReceiptPrinter {
    private final boolean printToConsole;
    
    public ReceiptPrinter(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }
    
    public String printReceipt(String recipe, int qty, Money subtotal, Money discount, Money tax, Money total) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(") x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }
        receipt.append("Tax (").append(10).append("%): ").append(tax).append("\n");
        receipt.append("Total: ").append(total);
        
        String out = receipt.toString();
        if (printToConsole) {
            System.out.println(out);
        }
        return out;
    }
}
