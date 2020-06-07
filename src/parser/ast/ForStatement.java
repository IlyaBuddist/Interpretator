package src.parser.ast;

public final class ForStatement implements Statement{
    private final Statement initialization;
    private final Expression termination;
    private final Statement increment;
    private final Statement statement;

    public ForStatement (Statement initialization, Expression termination, Statement increment,Statement block){
        this.initialization = initialization;
        this.termination = termination;
        this.increment = increment;
        this.statement = block;
    }
    public void execute(){
        for (initialization.execute();termination.eval().asNumber() !=0; increment.execute()){
            try {
                statement.execute();
            }catch (BreakStatement bs){
                break;
            } catch (ContinueStatement cs){
                //continue;
            }
        }
    }
    public String toString() {
        return "for" + initialization + ", " + termination + ", " + increment + " " + statement;
    }
}