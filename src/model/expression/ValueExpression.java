package model.expression;

import model.ADT.DictionaryInterface;
import model.type.Type;
import model.value.ValueInterface;

public class ValueExpression implements Expression {
	private final ValueInterface value;
	
	public ValueExpression(ValueInterface value) {
		this.value = value;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		return this.value;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += (this.value.toString());
		return representation;
	}

	@Override
	public Type typeCheck(DictionaryInterface<String, Type> typeEnvironment) throws Exception {
		return this.value.getType();
	}
}
