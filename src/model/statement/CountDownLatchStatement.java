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
    //var name
    //2) d
    private final String indexVariableName;
    private static Lock lock = new ReentrantLock();

    public CountDownLatchStatement(String indexVariableName){
        this.indexVariableName = indexVariableName;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();
        //we need the symbol table and the latch table

        //throw an exception if the variable is not defined
        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CountDownLatchStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }
        int latchIndexAsInteger = ((IntValue)(symbolTable.getValue(indexVariableName))).getValue();
        //the latch index must be defined
        if(!latchTable.isDefined(latchIndexAsInteger)){
            throw new UndefinedVariableException("CountDownLatchStatement: Variable " + indexVariableName + " is not a valid index in the latch table");
        }
        //we lock the thread
        lock.lock();
        //we get the value of the latch table index
        Integer latchValue = latchTable.getValue(latchIndexAsInteger);
        //we decrement the latch value by one
        if(latchValue > 0){
            latchTable.update(latchIndexAsInteger, latchValue - 1);
        }
        //modify the output
        crtState.getOutput().addLast(new StringValue("(latch) " + crtState.getThreadID()));
        //unlock the thread
        lock.unlock();
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("countDownLatch(" + indexVariableName + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        //variable must be defined
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CountDownLatchStatement: Variable " + indexVariableName + " is not defined in the typeEnvironment");
        }
        //defined variable must be of IntType
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("CountDownLatchStatement: Variable " + indexVariableName + " is not an integer");
        }
        return initialTypeEnvironment;
    }
}
