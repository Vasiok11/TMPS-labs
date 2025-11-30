package lab2.command;

// Command Pattern: Command Interface
public interface OrderCommand {
    void execute();
    void undo();
    String getDescription();
}
