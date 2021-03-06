package model.statement;

import java.io.BufferedReader;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.expression.Expression;
import model.type.IntType;
import model.type.StringType;
import model.type.Type;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class ReadFileStatement implements Statement {
	private final Expression filePath;
	private final String variableName;
	
	public ReadFileStatement(Expression filePath, String variableName) {
		this.filePath = filePath;
		this.variableName = variableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		
		if (!symbolTable.isDefined(this.variableName)) {
			throw new UndefinedVariableException("ReadFileStatement: " + this.variableName + " is not defined in the symbolTable");
		}
		
		DictionaryInterface<StringValue, BufferedReader> fileTable = crtState.getFileTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		ValueInterface filePathValue = this.filePath.evaluate(symbolTable, heap);
		
		// we know filePathValue is a StringValue, we can cast
		String filePathString = ((StringValue)filePathValue).getValue();
		if (!fileTable.isDefined((StringValue) filePathValue)) {
			throw new UndefinedVariableException("ReadFileStatement: File path " + filePathString + " is not defined in the file table");
		}
		
		BufferedReader fileBuffer = fileTable.getValue((StringValue)filePathValue);
		String currentLine = fileBuffer.readLine();
		if (currentLine == null) {
			symbolTable.update(this.variableName, new IntValue()); // default value for int = 0
		}
		else {
			symbolTable.update(this.variableName, new IntValue(Integer.parseInt(currentLine)));
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("readFile(" + this.filePath + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		if (!initialTypeEnvironment.getValue(this.variableName).equals(new IntType())) {
			throw new InvalidTypeException("ReadFileStatement: " + this.variableName + " is not an integer");
		}
		if (!this.filePath.typeCheck(initialTypeEnvironment).equals(new StringType())) {
			throw new InvalidTypeException("ReadFileStatement: file path should be a stringValue");
		}
		return initialTypeEnvironment;
	}
}
