package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.type.Type;

public class ConditionalAssignmentStatement implements Statement {
    //1 a)
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
        //get execution stack, pop the statement
        StackInterface<Statement> stack = crtState.getExecutionStack();
        //create the If statement
        stack.push(new IfStatement(conditionalExpression,
                //condition
                new AssignmentStatement(variableName, trueBranchExpression),
                //if branch
                new AssignmentStatement(variableName, falseBranchExpression)));
                //else branch
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += (variableName + " = " + conditionalExpression.toString() + " ? " + trueBranchExpression.toString()
                + " : " + falseBranchExpression.toString());
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        //no type checking is required
        return initialTypeEnvironment;
    }
}