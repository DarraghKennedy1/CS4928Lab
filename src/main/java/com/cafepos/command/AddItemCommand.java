package com.cafepos.command;

public final class AddItemCommand implements Command {
    private final OrderService service;
    private final String recipe;
    private final int quantity;

    public AddItemCommand(OrderService service, String recipe, int quantity) {
        this.service = service;
        this.recipe = recipe;
        this.quantity = quantity;
    }

    @Override
    public void execute() { service.addItem(recipe, quantity); }

    @Override
    public void undo() { service.removeLastItem(); }
}


