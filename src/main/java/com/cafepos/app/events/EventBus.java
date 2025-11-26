package com.cafepos.app.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Simple in-process event bus for decoupling components.
 * Allows components to communicate via publish/subscribe without tight coupling.
 */
public final class EventBus {
    private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    /**
     * Register a handler for events of a specific type.
     */
    public <T> void on(Class<T> type, Consumer<T> handler) {
        handlers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
    }

    /**
     * Emit an event to all registered handlers.
     */
    @SuppressWarnings("unchecked")
    public <T> void emit(T event) {
        var list = handlers.getOrDefault(event.getClass(), List.of());
        for (var h : list) {
            ((Consumer<T>) h).accept(event);
        }
    }
}
