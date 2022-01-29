package model.statement;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;

public class AwaitLatchStatement implements Statement{
    private final String indexVariableName;
    private static Lock lock = new ReentrantLock();

    public AwaitLatchStatement(String indexVariableName){
        this.indexVariableName = indexVariableName;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception{
        DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        StackInterface<Statement> stack = crtState.getExecutionStack();

        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("AwaitStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
        }
        int latchIndexAsInteger = ((IntValue)(symbolTable.getValue(indexVariableName))).getValue();
        if(!latchTable.isDefined(latchIndexAsInteger)){
            throw new UndefinedVariableException("AwaitStatement: Variable " + this.indexVariableName + " is not a valid index in the latch table");
        }
        lock.lock();
        Integer latchValue = latchTable.getValue(latchIndexAsInteger);
        if(latchValue != 0){
            stack.push(this);
        }
        lock.unlock();
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("awaitLatch(" + this.indexVariableName + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        return initialTypeEnvironment;
    }
}
