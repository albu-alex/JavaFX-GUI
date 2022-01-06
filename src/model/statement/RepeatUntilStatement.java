package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.type.BoolType;
import model.type.Type;

public class RepeatUntilStatement implements Statement {
	private final Statement statement;
	private final Expression conditionalExpression;

	public RepeatUntilStatement(Statement statement, Expression conditionalExpression) {
		this.statement = statement;
		this.conditionalExpression = conditionalExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<Statement> stack = crtState.getExecutionStack();
		stack.push(new WhileStatement(this.conditionalExpression, this.statement, false));
		return this.statement.execute(crtState);
	}
	
	public String toString() {
		String representation = "";
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
		representation += ("repeat {\n" + this.statement.toString() + "} ");
		representation += (" until (" + this.conditionalExpression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		if (this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType()) == false) {
			throw new InvalidTypeException("RepeatUntilStatement: Conditional expression is not boolean");
		}
		this.statement.getTypeEnvironment(initialTypeEnvironment.clone());
		return initialTypeEnvironment;
	}
}
