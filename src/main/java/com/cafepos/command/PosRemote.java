package com.cafepos.command;

import java.util.ArrayDeque;
import java.util.Deque;

public final class PosRemote {
    private final Command[] slots;
    private final Deque<Command> history = new ArrayDeque<>();

    public PosRemote(int numSlots) {
        if (numSlots <= 0) numSlots = 4;
        this.slots = new Command[numSlots];
        for (int i = 0; i < this.slots.length; i++) {
            this.slots[i] = () -> {};
        }
    }

    public void setButton(int slot, Command command) {
        if (slot < 0 || slot >= slots.length) throw new IllegalArgumentException("invalid slot");
        slots[slot] = command == null ? (() -> {}) : command;
    }

    public void press(int slot) {
        if (slot < 0 || slot >= slots.length) return;
        Command c = slots[slot];
        c.execute();
        history.push(c);
    }

    public void undoLast() {
        if (history.isEmpty()) return;
        Command last = history.pop();
        last.undo();
    }
}


