package com.cafepos.command;

public final class RemoveLastItemCommand implements Command {
    private final OrderService service;

    public RemoveLastItemCommand(OrderService service) {
        this.service = service;
    }

    @Override
    public void execute() { service.removeLastItem(); }
}


