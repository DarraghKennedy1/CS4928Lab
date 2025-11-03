package com.cafepos.command;

public final class MacroCommand implements Command {
    private final Command[] commands;

    public MacroCommand(Command... commands) {
        this.commands = commands == null ? new Command[0] : commands;
    }

    @Override
    public void execute() {
        for (Command c : commands) if (c != null) c.execute();
    }
}


