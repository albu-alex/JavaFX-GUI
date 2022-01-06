package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.ValueInterface;

public class IfStatement implements Statement {
	private final Expression conditionalExpression;
	private final Statement trueConditionStatement;
	private final Statement falseConditionStatement;
	
	public IfStatement(Expression conditionalExpression, Statement trueConditionStatement, Statement falseConditionStatement) {
		this.conditionalExpression = conditionalExpression;
		this.trueConditionStatement = trueConditionStatement;
		this.falseConditionStatement = falseConditionStatement;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<Statement> stack = crtState.getExecutionStack();
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		ValueInterface conditionalExpressionValue = this.conditionalExpression.evaluate(symbolTable, heap);
		if (((BoolValue)conditionalExpressionValue).getValue() == true) { // the expression is true => go to the first branch
			stack.push(this.trueConditionStatement);
		}
		else { // the expression is false => go to the second branch
			stack.push(this.falseConditionStatement);
		}
		
		return null;
	}
	
	public String toString() {
		String representation = "";
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the if statement
		representation += ("if ("+ this.conditionalExpression.toString() + ") {\n\t");
		representation += (this.trueConditionStatement.toString() + "}\n");
		representation += ("else {\n\t" + this.falseConditionStatement.toString() + "}\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		if (this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType()) == false) {
			throw new InvalidTypeException("IfStatement: Conditional expression is not boolean");
		}
		this.trueConditionStatement.getTypeEnvironment(initialTypeEnvironment.clone());
		this.falseConditionStatement.getTypeEnvironment(initialTypeEnvironment.clone());
		return initialTypeEnvironment;
	}
}
