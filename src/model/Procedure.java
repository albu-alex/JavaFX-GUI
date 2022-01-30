package model;
import java.util.ArrayList;

import model.statement.Statement;
import model.type.Type;

public class Procedure {
    private final ArrayList<Type> argumentTypes;
    private final ArrayList<String> argumentNames;
    // the lengths of these 2 lists are assumed to be equal
    private final Statement procedureStatement;

    public Procedure(ArrayList<Type> argumentTypes, ArrayList<String> argumentNames, Statement procedureStatement) {
        this.argumentTypes = argumentTypes;
        this.argumentNames = argumentNames;
        this.procedureStatement = procedureStatement;
    }

    public ArrayList<Type> getArgumentTypes() {
        return argumentTypes;
    }

    public ArrayList<String> getArgumentNames() {
        return argumentNames;
    }

    public Statement getProcedureStatement() {
        return procedureStatement;
    }

    @Override
    public String toString() {
        String representation = "";

        representation += "(";
        int pos;
        for (pos = 0; pos < argumentTypes.size() - 1; pos++) {
            representation += this.argumentTypes.get(pos).toString() + " " + this.argumentNames.get(pos) + ", ";
        }
        representation += this.argumentTypes.get(pos).toString() + " " + this.argumentNames.get(pos) + ")\n{\n";
        representation += this.procedureStatement.toString() + "}\n";

        return representation;
    }
}
