package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.type.Type;

public class ConditionalAssignmentStatement implements Statement {
    private final String variableName;
    private final Expression conditionalExpression;
    private final Expression trueBranchExpression;
    private final Expression falseBranchExpression;

    public ConditionalAssignmentStatement(String variableName, Expression conditionalExpression, Expression trueBranchExpression, Expression falseBranchExpression){
        this.variableName = variableName;
        this.conditionalExpression = conditionalExpression;
        this.trueBranchExpression = trueBranchExpression;
        this.falseBranchExpression = falseBranchExpression;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        StackInterface<Statement> stack = crtState.getExecutionStack();
        stack.push(new IfStatement(this.conditionalExpression,
                new AssignmentStatement(this.variableName, this.trueBranchExpression),
                new AssignmentStatement(this.variableName, this.falseBranchExpression)));
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += (this.variableName + " = " + this.conditionalExpression.toString() + " ? " + this.trueBranchExpression.toString() + " : " + this.falseBranchExpression.toString());
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        return initialTypeEnvironment;
    }
}