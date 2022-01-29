package model.statement;

import exception.AlreadyDefinedVariableException;
import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.expression.RelationalExpression;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;

public class ForStatement implements Statement {
	private final String variableName;
	private final Statement variableDeclarationStatement;
	private final Expression initialExpression; // for(int v = initialExpression...)
	private final Expression conditionalExpression; // has to be a relational expression
	private final Statement finalStatement; // has to be an increment statement
	private final Statement bodyStatement; // the one inside the curly brackets

	public ForStatement(String variableName,
						Expression initialExpression,
						Expression conditionalExpression,
						Statement finalStatement,
						Statement bodyStatement) {
		this.variableName = variableName;
		this.variableDeclarationStatement = new VariableDeclarationStatement(this.variableName, new IntType());
		this.initialExpression = initialExpression;
		this.conditionalExpression = conditionalExpression;
		this.finalStatement = finalStatement;
		this.bodyStatement = bodyStatement;
	}

	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		// we need a rel. expr because it checks if its operands are integers
		if (!(this.conditionalExpression instanceof RelationalExpression)) {
			throw new InvalidTypeException("ForStatement: Conditional expression is not a RelationalExpression");
		}
		if (!(this.finalStatement instanceof IncrementStatement)) {
			throw new InvalidTypeException("ForStatement: FinalStatement is not an IncrementStatement");
		}

		StackInterface<Statement> stack = crtState.getExecutionStack();
		if (stack.size() > 0) {
			stack.push(new ClearOutOfScopeVariablesStatement(crtState.getSymbolTable().clone()));
		}

		stack.push(new WhileStatement(
				this.conditionalExpression,
				new CompoundStatement(this.bodyStatement, this.finalStatement)
		));
		stack.push(new AssignmentStatement(this.variableName, this.initialExpression));

		return this.variableDeclarationStatement.execute(crtState);
	}

	public String toString() {
		String representation = "";

		// I have to do this in order to remove the "\n" and the ";" at the end of each statement
		String initialStatementString = this.variableDeclarationStatement.toString();
		initialStatementString = initialStatementString.substring(0, initialStatementString.length() - 2);
		String finalStatementString = this.finalStatement.toString();
		finalStatementString = finalStatementString.substring(0, finalStatementString.length() - 2);

		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
		representation += ("for (" + initialStatementString + " = " + this.initialExpression.toString() + "; " + this.conditionalExpression.toString() + "; " + finalStatementString + ") {\n\t");
		representation += (this.bodyStatement.toString() + "}\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.variableName)) {
			throw new AlreadyDefinedVariableException("ForStatement: variable " + this.variableName + " is already defined in the type environment");
		}
		initialTypeEnvironment = this.variableDeclarationStatement.getTypeEnvironment(initialTypeEnvironment);

		if (!this.initialExpression.typeCheck(initialTypeEnvironment).equals(new IntType())) {
			throw new InvalidTypeException("ForStatement: Initial assignment expression is not integer");
		}
		if (!this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType())) {
			throw new InvalidTypeException("ForStatement: The conditional expression is not boolean");
		}
		this.finalStatement.getTypeEnvironment(initialTypeEnvironment);
		this.bodyStatement.getTypeEnvironment(initialTypeEnvironment.clone());

		return initialTypeEnvironment;
	}
}
