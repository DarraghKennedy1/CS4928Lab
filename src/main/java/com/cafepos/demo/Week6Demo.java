package com.cafepos.demo;

import com.cafepos.checkout.OrderManager;
import com.cafepos.checkout.OrderManagerFactory;
import com.cafepos.smells.OrderManagerGod;

public final class Week6Demo {
    public static void main(String[] args) {
        // Old behavior (smelly)
        String oldReceipt = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);
        
        // New behavior (clean)
        OrderManager orderManager = OrderManagerFactory.createForCardPayment("LOYAL5");
        String newReceipt = orderManager.process("LAT+L", 2, false);
        
        System.out.println("Old Receipt:\n" + oldReceipt);
        System.out.println("\nNew Receipt:\n" + newReceipt);
        System.out.println("\nMatch: " + oldReceipt.equals(newReceipt));
    }
}
