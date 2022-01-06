package model.expression;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.ValueInterface;

public class LogicalExpression implements Expression {
	private final Expression firstExp;
	private final Expression secondExp;
	private final String operator;
	
	public LogicalExpression(Expression firstExp, Expression secondExp, String operator){
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		ValueInterface firstVal, secondVal;
		
		firstVal = this.firstExp.evaluate(symbolTable, heap);
		secondVal = this.secondExp.evaluate(symbolTable, heap);
		boolean firstBoolean = ((BoolValue)firstVal).getValue();
		boolean secondBoolean = ((BoolValue)secondVal).getValue();
		
		if (this.operator == "&&") {
			return new BoolValue(firstBoolean && secondBoolean);
		}
		if (this.operator == "||") {
			return new BoolValue(firstBoolean || secondBoolean);
		}
		else {
			throw new InvalidOperatorException("LogicalExpression: Invalid operator");
		}
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += (this.firstExp.toString());
		representation += (" " + this.operator + " ");
		representation += (this.secondExp.toString());
		return representation;
	}

	@Override
	public Type typeCheck(DictionaryInterface<String, Type> typeEnvironment) throws Exception {
		Type firstType, secondType, boolType;
		firstType = this.firstExp.typeCheck(typeEnvironment);
		secondType = this.secondExp.typeCheck(typeEnvironment);
		boolType = new BoolType();
		
		if (firstType.equals(boolType) == false) {
			throw new InvalidTypeException("LogicalExpression: First expression is not a boolean");
		}
		if (secondType.equals(boolType) == false) {
			throw new InvalidTypeException("LogicalExpression: Second expression is not a boolean");
		}
		
		return boolType;
	}
}
