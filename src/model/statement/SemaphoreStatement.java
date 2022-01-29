package model.statement;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyLockTable;
import model.expression.Expression;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;


public class SemaphoreStatement implements Statement{
    private final String indexVariableName;
    private final Expression totalPermitCountExpression;
    private static Lock lock = new ReentrantLock();

    public SemaphoreStatement(String indexVariableName, Expression totalPermitCountExpression){
        this.indexVariableName = indexVariableName;
        this.totalPermitCountExpression = totalPermitCountExpression;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
        DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = crtState.getSemaphoreTable();

        if(!symbolTable.isDefined(this.indexVariableName)){
            throw new UndefinedVariableException("CreateSemaphoreStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }

        ValueInterface totalPermitCount = this.totalPermitCountExpression.evaluate(symbolTable, heap);
        lock.lock();
        int newPositionInSemaphoreTable = ((MyLockTable<Integer, Pair<Integer, ArrayList<Integer>>>)(semaphoreTable)).getFirstAvailablePosition();
        semaphoreTable.insert(newPositionInSemaphoreTable, new Pair<>(((IntValue)totalPermitCount).getValue(), new ArrayList<>()));
        symbolTable.update(indexVariableName, new IntValue(newPositionInSemaphoreTable));
        lock.unlock();

        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("semaphore(" + this.indexVariableName + ", " + this.totalPermitCountExpression.toString() + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CreateSemaphoreStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("CreateSemaphoreStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        if(!totalPermitCountExpression.typeCheck(initialTypeEnvironment).equals(new IntType())){
            throw new InvalidTypeException("CreateSemaphoreStatement: Total permit count expression is not integer");
        }
        return initialTypeEnvironment;
    }
}
