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

public class CreateBarrierStatement implements Statement{
    private final String indexVariableName;
    private final Expression capacity;
    private static Lock lock = new ReentrantLock();

    public CreateBarrierStatement(String indexVariableName, Expression capacity){
        this.indexVariableName = indexVariableName;
        this.capacity = capacity;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();

        ValueInterface capacity = this.capacity.evaluate(symbolTable, heap);

        lock.lock();
        DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = crtState.getBarrierTable();
        int newPositionInBarrierTable = ((MyLockTable<Integer, Pair<Integer, ArrayList<Integer>>>)(barrierTable)).getFirstAvailablePosition();
        barrierTable.insert(newPositionInBarrierTable, new Pair<>(((IntValue)capacity).getValue(), new ArrayList<>()));
        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CreateBarrierStatement: variable " + indexVariableName + " is not defined in the symbol table");
        }
        symbolTable.update(indexVariableName, new IntValue(newPositionInBarrierTable));
        lock.unlock();
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("createBarrier(" + this.indexVariableName + ", " + capacity.toString() + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CreateBarrierStatement: variable " + indexVariableName + " is not defined in the type environment");
        }
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("CreateBarrierStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        if(!capacity.typeCheck(initialTypeEnvironment).equals(new IntType())){
            throw new InvalidTypeException("CreateBarrierStatement: Capacity expression is not integer");
        }
        return initialTypeEnvironment;
    }
}