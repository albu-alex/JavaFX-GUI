package model.statement;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.Type;
import model.value.ValueInterface;

public class ClearOutOfScopeVariablesStatement implements Statement {
    private final DictionaryInterface<String, ValueInterface> initialSymbolTable;

    public ClearOutOfScopeVariablesStatement(DictionaryInterface<String, ValueInterface> initialSymbolTable) {
        this.initialSymbolTable = initialSymbolTable;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable(); // the sym table after executing the inner scope
        symbolTable.setContent((HashMap<String, ValueInterface>)symbolTable.getAllPairs().entrySet()
                .stream()
                .filter(nameTypePair -> this.initialSymbolTable.isDefined(nameTypePair.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return crtState.getExecutionStack().pop().execute(crtState);
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        return initialTypeEnvironment;
    }
}
