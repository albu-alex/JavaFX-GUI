package model.statement;

import exception.EmptyADTException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.type.Type;
import model.value.ValueInterface;

public class ReturnStatement implements Statement{
    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        StackInterface<DictionaryInterface<String, ValueInterface>> symbolTable = crtState.getSymbolTableStack();
        if (symbolTable.size() == 0) {
            throw new EmptyADTException("ReturnStatement: there is no procedure to return from");
        }
        symbolTable.pop();

        return null;
    }

    @Override
    public String toString() {
        String representation = "";
        representation += ("return;\n");
        return representation;
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        return initialTypeEnvironment;
    }
}
