package model.statement;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.LockNotAcquiredException;
import exception.UndefinedVariableException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;

public class ReleaseStatement implements Statement {
    private final String indexVariableName;
    private static Lock lock = new ReentrantLock();

    ReleaseStatement(String indexVariableName){
        this.indexVariableName = indexVariableName;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("release(" + this.indexVariableName + ");\n");
        return representation;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = crtState.getSemaphoreTable();

        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }
        int semaphoreIndexAsInteger = ((IntValue)symbolTable.getValue(indexVariableName)).getValue();
        if(!semaphoreTable.isDefined(semaphoreIndexAsInteger)){
            throw new UndefinedVariableException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not a valid index in the semaphore table");
        }

        Pair<Integer, ArrayList<Integer>> semaphoreValue = semaphoreTable.getValue(semaphoreIndexAsInteger);
        Integer totalPermitCount = semaphoreValue.getKey();
        ArrayList<Integer> currentThreadsWithPermit = semaphoreValue.getValue();
        lock.lock();
        if(!currentThreadsWithPermit.contains(crtState.getThreadID())){
            throw new LockNotAcquiredException("ReleasePermitStatement: Thread " + crtState.getThreadID() + " doesn't have a permit from semaphore " + this.indexVariableName);
        }
        currentThreadsWithPermit.remove(Integer.valueOf(crtState.getThreadID()));
        semaphoreTable.update(semaphoreIndexAsInteger, new Pair<>(totalPermitCount, currentThreadsWithPermit));
        lock.unlock();

        return null;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        return initialTypeEnvironment;
    }
}
