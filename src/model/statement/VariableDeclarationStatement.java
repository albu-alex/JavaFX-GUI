package model.statement;

import exception.AlreadyDefinedVariableException;
import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.StringType;
import model.type.Type;
import model.value.ValueInterface;

public class VariableDeclarationStatement implements Statement {
	private final String variableName;
	private final Type variableType;
	
	public VariableDeclarationStatement(String variableName, Type variableType) {
		this.variableName = variableName;
		this.variableType = variableType;
	}

	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		
		if (symbolTable.isDefined(this.variableName)) {
			throw new AlreadyDefinedVariableException("VariableDeclarationStatement: Variable " + this.variableName + " is already defined");
		}
		
		if (this.variableType.equals(new IntType())) { // variable is of type int
			symbolTable.insert(this.variableName, this.variableType.getDefaultValue());
		}
		else if(this.variableType.equals(new BoolType())) { // variable is of type bool
			symbolTable.insert(this.variableName, this.variableType.getDefaultValue());
		}
		else if(this.variableType.equals(new StringType())) {
			symbolTable.insert(this.variableName, this.variableType.getDefaultValue());
		}
		else if (this.variableType instanceof ReferenceType) {
			symbolTable.insert(this.variableName, this.variableType.getDefaultValue());
		}
		else {
			throw new InvalidTypeException("VariableDeclarationStatement: Invalid type when trying to declare " + this.variableName);
		}
		
		return null;
	}
	
	public String toString() {
		String representation = "";
		representation += (this.variableType.toString() + " " + this.variableName + ";\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		initialTypeEnvironment.insert(this.variableName, this.variableType);
		return initialTypeEnvironment;
	}
}
