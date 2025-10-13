package com.cafepos.factory;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.SizeLarge;
import com.cafepos.decorator.Syrup;
import com.cafepos.domain.SimpleProduct;

public final class ProductFactory {
    public Product create(String recipe) {
        if (recipe == null || recipe.isBlank()) throw new IllegalArgumentException("recipe required");
        String[] raw = recipe.split("\\+");
        String[] parts = java.util.Arrays.stream(raw)
                .map(String::trim)
                .map(String::toUpperCase)
                .toArray(String[]::new);

        Product p = switch (parts[0]) {
            case "ESP" -> new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
            case "LAT" -> new SimpleProduct("P-LAT", "Latte", Money.of(3.20));
            case "CAP" -> new SimpleProduct("P-CAP", "Cappuccino", Money.of(3.00));
            default -> throw new IllegalArgumentException("Unknown base: " + parts[0]);
        };

        for (int i = 1; i < parts.length; i++) {
            String t = parts[i];
            p = switch (t) {
                case "SHOT" -> new ExtraShot(p);
                case "OAT" -> new OatMilk(p);
                case "SYP" -> new Syrup(p);
                case "L" -> new SizeLarge(p);
                default -> throw new IllegalArgumentException("Unknown addon: " + t);
            };
        }
        return p;
    }
}


