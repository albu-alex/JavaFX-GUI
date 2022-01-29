package model.statement;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.LockNotAcquiredException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;

public class UnlockStatement implements Statement{
    private final String indexVariableName;
    private static Lock lock = new ReentrantLock();

    public UnlockStatement(String indexVariableName){
        this.indexVariableName = indexVariableName;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, Integer> lockTable = crtState.getLockTable();

        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("UnlockStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }
        int lockIntegerAsIndex = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
        if(!lockTable.isDefined(lockIntegerAsIndex)){
            throw new UndefinedVariableException("UnlockStatement: Variable " + this.indexVariableName + " is not a valid index in the lock table");
        }
        lock.lock();
        Integer lockThread = lockTable.getValue(lockIntegerAsIndex);
        if(lockThread != crtState.getThreadID()){
            throw new LockNotAcquiredException("UnlockStatement: thread " + Integer.toString(crtState.getThreadID()) + " is not the owner of the lock " + this.indexVariableName);
        }
        lockTable.update(lockIntegerAsIndex, -1);
        lock.unlock();

        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("unlock(" + this.indexVariableName + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if (!initialTypeEnvironment.isDefined(this.indexVariableName)) {
            throw new UndefinedVariableException("UnlockStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        if (!initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType())) {
            throw new InvalidTypeException("UnlockStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        return initialTypeEnvironment;
    }
}
