package model.statement;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class CountDownLatchStatement implements Statement{
    private final String indexVariableName;
    private static Lock lock = new ReentrantLock();

    public CountDownLatchStatement(String indexVariableName){
        this.indexVariableName = indexVariableName;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();

        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CountDownLatchStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }
        int latchIndexAsInteger = ((IntValue)(symbolTable.getValue(indexVariableName))).getValue();
        if(!latchTable.isDefined(latchIndexAsInteger)){
            throw new UndefinedVariableException("CountDownLatchStatement: Variable " + this.indexVariableName + " is not a valid index in the latch table");
        }
        lock.lock();
        Integer latchValue = latchTable.getValue(latchIndexAsInteger);
        if(latchValue > 0){
            latchTable.update(latchIndexAsInteger, latchValue - 1);
        }
        crtState.getOutput().addLast(new StringValue("(latch) " + crtState.getThreadID()));
        lock.unlock();
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("countDownLatch(" + this.indexVariableName + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CountDownLatchStatement: Variable " + indexVariableName + " is not defined in the typeEnvironment");
        }
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("CountDownLatchStatement: Variable " + indexVariableName + " is not an integer");
        }
        return initialTypeEnvironment;
    }
}
