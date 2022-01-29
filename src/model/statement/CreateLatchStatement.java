package model.statement;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyLockTable;
import model.expression.Expression;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;

public class CreateLatchStatement implements Statement{
    private final String indexVariableName;
    private final Expression countExpression;
    private static Lock lock = new ReentrantLock();

    public CreateLatchStatement(String indexVariableName, Expression countExpression){
        this.indexVariableName = indexVariableName;
        this.countExpression = countExpression;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();
        DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();

        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CreateLatchStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }
        ValueInterface count = countExpression.evaluate(symbolTable, heap);
        lock.lock();
        int newPositionInLatchTable = ((MyLockTable<Integer, Integer>)(latchTable)).getFirstAvailablePosition();
        latchTable.insert(newPositionInLatchTable, ((IntValue)(count)).getValue());
        symbolTable.update(indexVariableName, new IntValue(newPositionInLatchTable));
        lock.unlock();
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("createLatch(" + this.indexVariableName + ", " + this.countExpression.toString() + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CreateLatchStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("CreateLatchStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        if(!countExpression.typeCheck(initialTypeEnvironment).equals(new IntType())){
            throw new InvalidTypeException("CreateLatchStatement: Total permit count expression is not integer");
        }
        return initialTypeEnvironment;
    }
}
