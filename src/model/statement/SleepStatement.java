package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.expression.ValueExpression;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;

public class SleepStatement implements Statement{
    private final Expression expression;

    public SleepStatement(Expression expression){
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
        StackInterface<Statement> stack = crtState.getExecutionStack();

        int sleepTimeAsInteger = ((IntValue)expression.evaluate(symbolTable, heap)).getValue();
        if(sleepTimeAsInteger > 0){
            stack.push(new SleepStatement(new ValueExpression(new IntValue(sleepTimeAsInteger - 1))));
        }
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("sleep(" + expression.toString() + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if (!expression.typeCheck(initialTypeEnvironment).equals(new IntType())) {
            throw new InvalidTypeException("SleepStatement: sleep time should be an integer");
        }
        return initialTypeEnvironment;
    }
}
