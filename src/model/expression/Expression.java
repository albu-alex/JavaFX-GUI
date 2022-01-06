package model.expression;

import model.ADT.DictionaryInterface;
import model.type.Type;
import model.value.ValueInterface;

public interface Expression {
	public ValueInterface evaluate(
			DictionaryInterface<String, ValueInterface> symbolTable, 
			DictionaryInterface<Integer, ValueInterface> heap
			) throws Exception;
	public Type typeCheck(DictionaryInterface<String, Type> typeEnvironment) throws Exception;
	public String toString();
}
