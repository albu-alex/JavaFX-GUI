package model.statement;

import java.util.ArrayList;
import exception.InvalidProcedureArgumentException;
import exception.StackOverflowException;
import exception.UndefinedVariableException;
import model.Procedure;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyDictionary;
import model.expression.Expression;
import model.type.Type;
import model.value.ValueInterface;

public class CallProcedureStatement implements Statement{
    private final String procedureName;
    private final ArrayList<Expression> argumentValuesExpression;
    public final static int STACK_PROCEDURE_LIMIT = 256;

    public CallProcedureStatement(String procedureName, ArrayList<Expression> argumentValuesExpression) {
        this.procedureName = procedureName;
        this.argumentValuesExpression = argumentValuesExpression;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, Procedure> procedureTable = crtState.getProcedureTable();
        if (!procedureTable.isDefined(procedureName)) {
            throw new UndefinedVariableException("CallProcedureStatement: procedure " + procedureName + " does not exist");
        }

        Procedure procedureFrame = procedureTable.getValue(procedureName);
        if (argumentValuesExpression.size() != procedureFrame.getArgumentNames().size()) {
            throw new InvalidProcedureArgumentException("CallProcedureStatement: invalid number of arguments");
        }

        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
        ArrayList<Type> argumentTypes = procedureFrame.getArgumentTypes();
        ArrayList<String> argumentNames = procedureFrame.getArgumentNames();

        DictionaryInterface<String, ValueInterface> procedureSymbolTable = new MyDictionary<>();
        for (int pos = 0; pos < this.argumentValuesExpression.size(); pos++) {
            ValueInterface argumentValue = this.argumentValuesExpression.get(pos).evaluate(symbolTable, heap);
            if (!argumentValue.getType().equals(argumentTypes.get(pos))) {
                throw new InvalidProcedureArgumentException("CallProcedureStatement: invalid argument type on position " + Integer.toString(pos));
            }
            procedureSymbolTable.insert(argumentNames.get(pos), argumentValue);
        }

        if (crtState.getSymbolTableStack().size() >= CallProcedureStatement.STACK_PROCEDURE_LIMIT) {
            throw new StackOverflowException("CallProcedureStatement: maximum number of function calls exceeded");
        }

        crtState.getSymbolTableStack().push(procedureSymbolTable);
        crtState.getExecutionStack().push(new ReturnStatement());
        crtState.getExecutionStack().push(procedureFrame.getProcedureStatement());

        return null;
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();

        representation.append(this.procedureName).append("(");
        int pos;
        for (pos = 0; pos < this.argumentValuesExpression.size() - 1; pos++) {
            representation.append(this.argumentValuesExpression.get(pos).toString()).append(", ");
        }
        representation.append(this.argumentValuesExpression.get(pos).toString()).append(");\n");

        return representation.toString();
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        // the expression - argument type checks have to be done inside execute, because in this method
        // we don't have access to the crtState => we can't access the procedure table
        // however, the proc statement will work well as long as the expressions match the argument (when creating the
        // procedure, we type check the statement with the argument types)
        return initialTypeEnvironment;
    }
}
