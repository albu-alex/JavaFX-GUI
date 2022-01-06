package model.expression;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ADT.DictionaryInterface;
import model.type.ReferenceType;
import model.type.Type;
import model.value.ReferenceValue;
import model.value.ValueInterface;

public class HeapReadingExpression implements Expression {
	private final Expression expression;
	
	public HeapReadingExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {		
		ValueInterface expressionValue = this.expression.evaluate(symbolTable, heap);
		
		int heapAddress = ((ReferenceValue)expressionValue).getHeapAddress();
		if (heap.isDefined(heapAddress) == false) {
			throw new UndefinedVariableException("HeapReadingExpression: Undefined variable at address 0x" + Integer.toHexString(heapAddress));
		}
		return heap.getValue(heapAddress);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("*(" + this.expression.toString() + ")");
		return representation;
	}

	@Override
	public Type typeCheck(DictionaryInterface<String, Type> typeEnvironment) throws Exception {
		Type expressionType = this.expression.typeCheck(typeEnvironment);
		if (expressionType instanceof ReferenceType == false) {
			throw new InvalidTypeException("HeapReadingExpression: Expression is not a reference");
		}
		return ((ReferenceType)expressionType).getInnerType();
	}
}
