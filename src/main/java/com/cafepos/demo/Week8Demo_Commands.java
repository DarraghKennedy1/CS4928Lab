package com.cafepos.demo;

import com.cafepos.command.AddItemCommand;
import com.cafepos.command.RemoveLastItemCommand;
import com.cafepos.command.OrderService;
import com.cafepos.command.PayOrderCommand;
import com.cafepos.command.PosRemote;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.WalletPayment;
import com.cafepos.payment.PaymentStrategy;

import java.util.Scanner;

public final class Week8Demo_Commands {
    public static void main(String[] args) {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Week 8 Demo: Commands (CLI) ===");
        System.out.println("Order #" + order.id());

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("  1) Add item");
            System.out.println("  2) Undo last");
            System.out.println("  3) Pay order");
            System.out.println("  4) Show order");
            System.out.println("  0) Exit");
            System.out.print("Select: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    try {
                        System.out.print("Recipe (e.g. ESP, LAT+L, ESP+SHOT+OAT): ");
                        String recipe = sc.nextLine().trim();
                        System.out.print("Quantity: ");
                        int qty = Integer.parseInt(sc.nextLine().trim());
                        remote.setSlot(0, new AddItemCommand(service, recipe, qty));
                        remote.press(0);
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    }
                    break;
                case "2":
                    // Provide an explicit remove command bound to slot 1, then undo also works
                    remote.setSlot(1, new RemoveLastItemCommand(service));
                    remote.press(1);
                    break;
                case "3":
                    PaymentStrategy strategy = pickPayment(sc);
                    int tax = promptTax(sc);
                    remote.setSlot(2, new PayOrderCommand(service, strategy, tax));
                    remote.press(2);
                    break;
                case "4":
                    printOrderSummary(service);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Please choose a valid option.");
            }
        }
        System.out.println("Goodbye.");
    }

    private static int promptTax(Scanner sc) {
        System.out.print("Tax percent (e.g. 10): ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid number, defaulting to 10.");
            return 10;
        }
    }

    private static PaymentStrategy pickPayment(Scanner sc) {
        System.out.println("Payment method:");
        System.out.println("  1) Cash");
        System.out.println("  2) Card");
        System.out.println("  3) Wallet");
        System.out.print("Choose: ");
        String pm = sc.nextLine().trim();
        switch (pm) {
            case "1":
                return new CashPayment();
            case "2":
                System.out.print("Card number: ");
                String card = sc.nextLine().trim();
                return new CardPayment(card);
            case "3":
                System.out.print("Wallet id: ");
                String wid = sc.nextLine().trim();
                return new WalletPayment(wid);
            default:
                System.out.println("Unknown payment, defaulting to Cash.");
                return new CashPayment();
        }
    }

    private static void printOrderSummary(OrderService service) {
        Order order = service.order();
        System.out.println("\n=== Order Summary (#" + order.id() + ") ===");
        order.items().forEach(li -> System.out.println(" - " + li.product().name() + " x" + li.quantity() + " = " + li.lineTotal()));
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Total (10% tax): " + service.totalWithTax(10));
    }
}


