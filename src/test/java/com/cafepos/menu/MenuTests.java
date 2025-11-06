package com.cafepos.menu;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MenuTests {
    
    @Test
    void depth_first_iteration_collects_all_nodes() {
        Menu root = new Menu("ROOT");
        Menu a = new Menu("A");
        Menu b = new Menu("B");
        root.add(a);
        root.add(b);
        a.add(new MenuItem("x", Money.of(1.0), true));
        b.add(new MenuItem("y", Money.of(2.0), false));
        
        List<String> names = root.allItems().stream()
                .map(MenuComponent::name)
                .toList();
        
        assertTrue(names.contains("x"));
        assertTrue(names.contains("y"));
    }
    
    @Test
    void vegetarianItems_returns_only_vegetarian_items() {
        Menu root = new Menu("ROOT");
        root.add(new MenuItem("Veg Item", Money.of(1.0), true));
        root.add(new MenuItem("Non-Veg Item", Money.of(2.0), false));
        root.add(new MenuItem("Another Veg", Money.of(1.5), true));
        
        List<MenuItem> vegItems = root.vegetarianItems();
        
        assertEquals(2, vegItems.size());
        assertTrue(vegItems.stream().allMatch(MenuItem::vegetarian));
        assertTrue(vegItems.stream().anyMatch(mi -> mi.name().equals("Veg Item")));
        assertTrue(vegItems.stream().anyMatch(mi -> mi.name().equals("Another Veg")));
    }
    
    @Test
    void nested_menu_traversal_includes_all_levels() {
        Menu root = new Menu("ROOT");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
        drinks.add(coffee);
        root.add(drinks);
        
        List<MenuComponent> all = root.allItems();
        
        assertEquals(1, all.size());
        assertEquals("Espresso", all.get(0).name());
    }
    
    @Test
    void menuItem_throws_on_composite_operations() {
        MenuItem item = new MenuItem("Test", Money.of(1.0), false);
        
        assertThrows(UnsupportedOperationException.class, 
                () -> item.add(new MenuItem("x", Money.of(1.0), false)));
        assertThrows(UnsupportedOperationException.class, 
                () -> item.getChild(0));
    }
    
    @Test
    void menu_throws_on_leaf_operations() {
        Menu menu = new Menu("Test");
        
        assertThrows(UnsupportedOperationException.class, 
                () -> menu.price());
    }
}

