package model.statement;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.ValueInterface;

public class IncrementStatement implements Statement {
    private final int DEFAULT_INCREMENT_VALUE = 1;
    private final String variableName;
    private final String operator;
    private final int incrementValue;

    public IncrementStatement(String variableName, String operator) {
        this.variableName = variableName;
        this.operator = operator;
        this.incrementValue = this.DEFAULT_INCREMENT_VALUE;
    }

    public IncrementStatement(String variableName, String operator, int incrementValue) {
        this.variableName = variableName;
        this.operator = operator;
        this.incrementValue = incrementValue;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        if (!symbolTable.isDefined(this.variableName)) {
            throw new UndefinedVariableException("IncrementStatement: variable " + this.variableName + " is undefined in the symbolTable");
        }

        int previousValueAsInteger = ((IntValue)symbolTable.getValue(this.variableName)).getValue();
        if (operator.equals("+")) {
            symbolTable.update(this.variableName, new IntValue(previousValueAsInteger + this.incrementValue));
        }
        else if(operator.equals("-")) {
            symbolTable.update(this.variableName, new IntValue(previousValueAsInteger - this.incrementValue));
        }
        else {
            throw new InvalidOperatorException("IncrementStatement: operator should be either + or -, not " + operator);
        }

        return null;
    }

    public String toString() {
        String representation = "";
        representation += this.variableName;

        if (this.incrementValue == this.DEFAULT_INCREMENT_VALUE) {
            representation += (operator + operator + ";\n"); // beware that wrong operators will still be displayed here!
        }
        else {
            representation += (operator + "= " + this.incrementValue + ";\n");
        }

        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        if (!initialTypeEnvironment.isDefined(this.variableName)) {
            throw new UndefinedVariableException("IncrementStatement: variable " + this.variableName + " is undefined in the typeEnvironment");
        }
        if (!initialTypeEnvironment.getValue(this.variableName).equals(new IntType())) {
            throw new InvalidTypeException("IncrementStatement: variable " + this.variableName + " should be an integer");
        }
        return initialTypeEnvironment;
    }

}
