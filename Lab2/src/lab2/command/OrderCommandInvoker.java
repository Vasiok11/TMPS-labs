package lab2.command;

import java.util.Stack;

// Command Pattern: Invoker
public class OrderCommandInvoker {
    private final Stack<OrderCommand> commandHistory;
    private final Stack<OrderCommand> undoneCommands;

    public OrderCommandInvoker() {
        this.commandHistory = new Stack<>();
        this.undoneCommands = new Stack<>();
    }

    public void executeCommand(OrderCommand command) {
        command.execute();
        commandHistory.push(command);
        undoneCommands.clear(); // Clear redo stack when new command is executed
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            OrderCommand command = commandHistory.pop();
            command.undo();
            undoneCommands.push(command);
        } else {
            System.out.println("[INVOKER] No commands to undo.");
        }
    }

    public void redoLastCommand() {
        if (!undoneCommands.isEmpty()) {
            OrderCommand command = undoneCommands.pop();
            command.execute();
            commandHistory.push(command);
        } else {
            System.out.println("[INVOKER] No commands to redo.");
        }
    }

    public void printCommandHistory() {
        System.out.println("\n=== Command History ===");
        if (commandHistory.isEmpty()) {
            System.out.println("No commands in history.");
        } else {
            int index = 1;
            for (OrderCommand command : commandHistory) {
                System.out.println(index++ + ". " + command.getDescription());
            }
        }
        System.out.println("=======================\n");
    }

    public int getHistorySize() {
        return commandHistory.size();
    }

    public boolean canUndo() {
        return !commandHistory.isEmpty();
    }

    public boolean canRedo() {
        return !undoneCommands.isEmpty();
    }
}
