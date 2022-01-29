package model.statement;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.LockAlreadyAcquiredException;
import exception.UndefinedVariableException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;

public class AcquireStatement implements Statement{
    private final String indexVariableName;
    private static Lock lock = new ReentrantLock();

    public AcquireStatement(String indexVariableName) {
        this.indexVariableName = indexVariableName;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = crtState.getSemaphoreTable();
        StackInterface<Statement> stack = crtState.getExecutionStack();

        if (!symbolTable.isDefined(this.indexVariableName)) {
            throw new UndefinedVariableException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }

        int semaphoreIndexAsInteger = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
        if (!semaphoreTable.isDefined(semaphoreIndexAsInteger)) {
            throw new UndefinedVariableException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not a valid index in the semaphore table");
        }

        Pair<Integer, ArrayList<Integer>> semaphoreValue = semaphoreTable.getValue(semaphoreIndexAsInteger);
        Integer totalPermitCount = semaphoreValue.getKey();
        ArrayList<Integer> currentThreadsWithPermit = semaphoreValue.getValue();
        lock.lock();
        if (currentThreadsWithPermit.size() < totalPermitCount) {
            if (currentThreadsWithPermit.contains(crtState.getThreadID())) {
                throw new LockAlreadyAcquiredException("AcquirePermitStatement: Thread " + crtState.getThreadID() + " already has a permit from semaphore " + this.indexVariableName);
            }
            currentThreadsWithPermit.add(crtState.getThreadID());
            semaphoreTable.update(semaphoreIndexAsInteger, new Pair<>(totalPermitCount, currentThreadsWithPermit));
        }
        else {
            stack.push(this);
        }
        lock.unlock();

        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("acquire(" + this.indexVariableName + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if (!initialTypeEnvironment.isDefined(this.indexVariableName)) {
            throw new UndefinedVariableException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        if (!initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType())) {
            throw new InvalidTypeException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        return initialTypeEnvironment;
    }

}
