package src.parser.ast;

public final class ContinueStatement extends RuntimeException implements Statement {
    @Override
    public void execute() {
        throw this;
    }

    @Override
    public String toString() {
        return "continue";
    }
}