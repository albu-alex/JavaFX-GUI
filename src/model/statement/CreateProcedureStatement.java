package model.statement;

import java.util.ArrayList;
import exception.AlreadyDefinedVariableException;
import exception.InvalidProcedureArgumentException;
import model.Procedure;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.Type;

public class CreateProcedureStatement implements Statement{
    private final String procedureName;
    private final Procedure procedureFrame;

    public CreateProcedureStatement(String procedureName, Procedure procedureFrame) {
        this.procedureName = procedureName;
        this.procedureFrame = procedureFrame;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, Procedure> procedureTable = crtState.getProcedureTable();
        // we can't overload procedures
        if (procedureTable.isDefined(this.procedureName)) {
            throw new AlreadyDefinedVariableException("CreateProcedureStatement: Procedure " + this.procedureName + " is already defined in the procedure table");
        }

        procedureTable.insert(this.procedureName, this.procedureFrame);

        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("procedure " + this.procedureName + this.procedureFrame.toString());
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        ArrayList<Type> argumentTypes = this.procedureFrame.getArgumentTypes();
        ArrayList<String> argumentNames = this.procedureFrame.getArgumentNames();
        if(argumentTypes.size() != argumentNames.size()) {
            throw new InvalidProcedureArgumentException("CreateProcedureStatement: The argument list is invalid");
        }

        DictionaryInterface<String, Type> procedureTypeEnvironment = initialTypeEnvironment.clone();
        for (int pos = 0; pos < argumentTypes.size(); pos++) {
            if (procedureTypeEnvironment.isDefined(argumentNames.get(pos))) {
                throw new InvalidProcedureArgumentException("CreateProcedureStatement: argument name " + argumentNames.get(pos) + " is used more than once");
            }
            procedureTypeEnvironment.insert(argumentNames.get(pos), argumentTypes.get(pos));
        }
        this.procedureFrame.getProcedureStatement().getTypeEnvironment(procedureTypeEnvironment);

        return initialTypeEnvironment;
    }
}
