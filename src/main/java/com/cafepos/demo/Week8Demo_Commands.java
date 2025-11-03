package com.cafepos.demo;

import com.cafepos.command.AddItemCommand;
import com.cafepos.command.Command;
import com.cafepos.command.MacroCommand;
import com.cafepos.command.MarkReadyCommand;
import com.cafepos.command.OrderService;
import com.cafepos.command.PayOrderCommand;
import com.cafepos.command.PosRemote;
import com.cafepos.command.RemoveLastItemCommand;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.payment.CardPayment;

public final class Week8Demo_Commands {
    public static void main(String[] args) {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(4);

        // Program buttons
        remote.setButton(0, new AddItemCommand(service, "ESP+SHOT", 1));
        remote.setButton(1, new AddItemCommand(service, "LAT+L", 2));
        remote.setButton(2, new RemoveLastItemCommand(service));
        remote.setButton(3, new PayOrderCommand(service, new CardPayment("1234123412341234"), 10));

        // Macro: add espresso + add latte + pay
        Command macro = new MacroCommand(
                new AddItemCommand(service, "ESP", 1),
                new AddItemCommand(service, "LAT+L", 1),
                new PayOrderCommand(service, new CardPayment("1234123412341234"), 10)
        );

        System.out.println("=== Week 8 Demo: Commands ===");
        remote.press(0);
        remote.press(1);
        System.out.println("Undo last add:");
        remote.undoLast();
        System.out.println("Run macro:");
        macro.execute();
        System.out.println("Mark ready:");
        new MarkReadyCommand(service).execute();
    }
}


