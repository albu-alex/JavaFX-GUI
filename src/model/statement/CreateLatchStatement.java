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
    //var name
    //2 b)
    private final String indexVariableName;
    //expression
    private final Expression countExpression;
    private static Lock lock = new ReentrantLock();

    //CreateLatchStatement constructor
    public CreateLatchStatement(String indexVariableName, Expression countExpression){
        this.indexVariableName = indexVariableName;
        this.countExpression = countExpression;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();
        DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
        //for the expression evaluation, we need the heap and the symbol table, as shown in the question

        //if the variable that the latch statement uses is not defined, I throw an exception
        if(!symbolTable.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CreateLatchStatement: Variable " + indexVariableName + " is not defined in the symbolTable");
        }
        //we must store the result of the expression evaluation
        ValueInterface count = countExpression.evaluate(symbolTable, heap);
        //I use Java's lock mechanism
        lock.lock();
        //latchTable is of type DictionaryInterface and must be converted to a MyLockTable
        int newPositionInLatchTable = ((MyLockTable<Integer, Integer>)(latchTable)).getFirstAvailablePosition();
        //I insert into the latchTable the new available position in the latch table
        //with the value of the count, converted from an int to an IntValue, in order
        //to fit our interpreter
        latchTable.insert(newPositionInLatchTable, ((IntValue)(count)).getValue());
        //I also update the variable name in the symbol table with the new position
        //in the latch table
        symbolTable.update(indexVariableName, new IntValue(newPositionInLatchTable));
        //in order for the program to function properly, we must unlock the thread
        lock.unlock();
        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("createLatch(" + indexVariableName + ", " + countExpression.toString() + ");\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        //if the variable that the latch statement uses is not defined, I throw an exception
        if(!initialTypeEnvironment.isDefined(indexVariableName)){
            throw new UndefinedVariableException("CreateLatchStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
        }
        //if the variable is also not an integer, I throw an exception
        if(!initialTypeEnvironment.getValue(indexVariableName).equals(new IntType())){
            throw new InvalidTypeException("CreateLatchStatement: Variable " + this.indexVariableName + " is not an integer");
        }
        //also, the total permit count must be an integer
        if(!countExpression.typeCheck(initialTypeEnvironment).equals(new IntType())){
            throw new InvalidTypeException("CreateLatchStatement: Total permit count expression is not integer");
        }
        return initialTypeEnvironment;
    }
}
