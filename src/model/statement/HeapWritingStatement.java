package model.statement;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.expression.Expression;
import model.type.ReferenceType;
import model.type.Type;
import model.value.ReferenceValue;
import model.value.ValueInterface;

public class HeapWritingStatement implements Statement {
	private final String variableName;
	private final Expression expression;
	
	public HeapWritingStatement(String variableName, Expression expression) {
		this.variableName = variableName;
		this.expression = expression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		if (!symbolTable.isDefined(this.variableName)) {
			throw new UndefinedVariableException("HeapWritingStatement: " + this.variableName + " is not defined in the symbol table");
		}
		
		ValueInterface variableValue = symbolTable.getValue(this.variableName);
		int positionInHeap = ((ReferenceValue)variableValue).getHeapAddress();
		if (!heap.isDefined(positionInHeap)) {
			throw new UndefinedVariableException("HeapWritingStatement: Undefined variable at address 0x" + Integer.toHexString(positionInHeap));
		}
		
		ValueInterface expressionValue = this.expression.evaluate(symbolTable, heap);
		heap.update(positionInHeap, expressionValue);
		
		return null;
	}

	@Override
	public String toString() {
		String representation = "";
		representation += ("*(" + this.variableName + ") = " + this.expression.toString() + ";\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		Type expressionReferenceType = new ReferenceType(this.expression.typeCheck(initialTypeEnvironment));
		// the type of the reference that "variableName" is allocated to
		// if getValue does not return a ReferenceValue, the equals will fail; it will also fail if the inner types don't match
		// so we're doing both checks simultaneously
		if (!initialTypeEnvironment.getValue(this.variableName).equals(expressionReferenceType)) {
			throw new InvalidTypeException("HeapWritingStatement: Expression cannot be evaluated to a " + expressionReferenceType.toString());
		}
		return initialTypeEnvironment;
	}
}
