package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.type.BoolType;
import model.type.Type;

public class RepeatUntilStatement implements Statement{
    private final Statement statement;
    private final Expression expression;

    public RepeatUntilStatement(Statement statement, Expression expression){
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        StackInterface<Statement> stack = crtState.getExecutionStack();
        stack.push(new WhileStatement(expression, statement, false));
        stack.push(statement);
        return null;
    }

    public String toString() {
        String representation = "";
        // this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
        representation += ("repeat {\n" + statement.toString() + "} ");
        representation += (" until (" + expression.toString() + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if(!expression.typeCheck(initialTypeEnvironment).equals(new BoolType())){
            throw new InvalidTypeException("RepeatUntilStatement: Conditional expression is not boolean");
        }
        statement.getTypeEnvironment(initialTypeEnvironment.clone());
        return initialTypeEnvironment;
    }
}
