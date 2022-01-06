package model.expression;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class RelationalExpression implements Expression {
	private final Expression firstExp;
	private final Expression secondExp;
	private final String operator;
	
	public RelationalExpression(Expression firstExp, Expression secondExp, String operator) {
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}
	
	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		ValueInterface firstVal, secondVal;
		
		firstVal = this.firstExp.evaluate(symbolTable, heap);
		secondVal = this.secondExp.evaluate(symbolTable, heap);
		int firstInt = ((IntValue)firstVal).getValue();
		int secondInt = ((IntValue)secondVal).getValue();
		
		if (this.operator.equals("<")) {
			return new BoolValue(firstInt < secondInt);
		}
		if (this.operator.equals("<=")) {
			return new BoolValue(firstInt <= secondInt);
		}
		if (this.operator.equals(">")) {
			return new BoolValue(firstInt > secondInt);
		}
		if (this.operator.equals(">=")) {
			return new BoolValue(firstInt >= secondInt);
		}
		if (this.operator.equals("==")) {
			return new BoolValue(firstInt == secondInt);
		}
		if (this.operator.equals("!=")) {
			return new BoolValue(firstInt != secondInt);
		}
		else {
			throw new InvalidOperatorException("RelationalExpression: Invalid operator");
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
		Type firstType, secondType, intType;
		firstType = this.firstExp.typeCheck(typeEnvironment);
		secondType = this.secondExp.typeCheck(typeEnvironment);
		intType = new IntType();
		
		if (firstType.equals(intType) == false) {
			throw new InvalidTypeException("RelationalExpression: First expression is not an integer");
		}
		if (secondType.equals(intType) == false) {
			throw new InvalidTypeException("RelationalExpression: Second expression is not an integer");
		}
		
		return new BoolType();
	}

}
