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

public class WhileStatement implements Statement {
	private final Expression conditionalExpression;
	private final Statement statement;
	private final boolean expectedLogicalValue; // this is used so that we can have conditions like 'while (x == false)'
	
	public WhileStatement(Expression conditionalExpression, Statement statement) {
		this.conditionalExpression = conditionalExpression;
		this.statement = statement;
		this.expectedLogicalValue = true;
	}
	
	public WhileStatement(Expression conditionalExpression, Statement statement, boolean expectedLogicalValue) {
		this.conditionalExpression = conditionalExpression;
		this.statement = statement;
		this.expectedLogicalValue = expectedLogicalValue;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<Statement> stack = crtState.getExecutionStack();
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		ValueInterface conditionalExpressionValue = this.conditionalExpression.evaluate(symbolTable, heap);
		if (((BoolValue)conditionalExpressionValue).getValue() == expectedLogicalValue) {
			stack.push(this);
			return this.statement.execute(crtState);
		}
		
		return null;
	}
	
	public String toString() {
		String representation = "";
		String negationSymbolString = "";
		if (!this.expectedLogicalValue) {
			negationSymbolString += "! ";
		}
		representation += ("while (" + negationSymbolString + this.conditionalExpression.toString() + ") {\n\t");
		representation += (this.statement.toString() + "}\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		if (!this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType())) {
			throw new InvalidTypeException("WhileStatement: Conditional expression is not boolean");
		}
		this.statement.getTypeEnvironment(initialTypeEnvironment.clone());
		return initialTypeEnvironment;
	}
}
