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
    //var name
    //2) c
    private final String indexVariableName;
    private static Lock lock = new ReentrantLock();

    //AwaitLatchStatement constructor
    public AwaitLatchStatement(String indexVariableName){
        this.indexVariableName = indexVariableName;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception{
        DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        StackInterface<Statement> stack = crtState.getExecutionStack();
        //we do not need the evaluation of the expression, but we need the symbol table and the stack

        //the variable must be defined in the symbol table
        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("AwaitLatchStatement: Variable " + indexVariableName + " is not defined in the symbolTable");
        }
        //we transform the latch index into an integer
        int latchIndexAsInteger = ((IntValue)(symbolTable.getValue(indexVariableName))).getValue();
        //we must check the if the index is defined in the latch table
        if(!latchTable.isDefined(latchIndexAsInteger)){
            throw new UndefinedVariableException("AwaitLatchStatement: Variable " + indexVariableName + " is not a valid index in the latch table");
        }
        //we use the java lock mechanism
        lock.lock();
        //if the latch value is different to 0, we push the statement ot the stack
        Integer latchValue = latchTable.getValue(latchIndexAsInteger);
        if(latchValue != 0){
            stack.push(this);
        }
        //otherwise, we do nothing and unlock the locked thread
        lock.unlock();
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("awaitLatch(" + indexVariableName + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        //the variable must be defined
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("AwaitLatchStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        //the defined variable must be an int
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("AwaitLatchStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        return initialTypeEnvironment;
    }
}
