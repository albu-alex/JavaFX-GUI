package model.statement;

import java.util.ArrayList;
import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.Expression;
import model.type.Type;
import model.value.ValueInterface;

public class SwitchStatement implements Statement{
    private final Expression switchExpression;
    private final ArrayList<Expression> caseExpressionList;
    private final ArrayList<Statement> caseStatementList;

    public SwitchStatement(Expression switchExpression, ArrayList<Expression> caseExpressionList, ArrayList<Statement> caseStatementList){
        this.switchExpression = switchExpression;
        this.caseExpressionList = caseExpressionList;
        this.caseStatementList = caseStatementList;
    }

    @Override
    public ProgramState execute(ProgramState crtState) throws Exception {
        if(caseExpressionList.size() + 1 != caseStatementList.size()){
            throw new Exception("SwitchStatement: number of case expressions does not match the number of case statements");
        }

        DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
        DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
        StackInterface<Statement> stack = crtState.getExecutionStack();
        if(stack.size() > 0){
            stack.push(new ClearOutOfScopeVariablesStatement(symbolTable.clone()));
        }
        int pos;
        ValueInterface switchExpressionValue = switchExpression.evaluate(symbolTable, heap);
        for(pos = 0; pos < caseExpressionList.size(); pos++){
            if(caseExpressionList.get(pos).evaluate(symbolTable, heap).equals(switchExpressionValue)){
                stack.push(caseStatementList.get(pos));
                return null;
            }
        }
        stack.push(caseStatementList.get(pos));
        return null;
    }
    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append("switch(").append(this.switchExpression.toString()).append(")\n");

        int pos;
        for (pos = 0; pos < this.caseExpressionList.size(); pos++) {
            representation.append("case (").append(this.caseExpressionList.get(pos).toString()).append(") \n{\n").append(this.caseStatementList.get(pos)).append("}\n");
        }
        representation.append("default\n{\n").append(this.caseStatementList.get(pos)).append("}\n");

        return representation.toString();
    }

    @Override
    public DictionaryInterface<String, Type> getTypeEnvironment(
            DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
        Type switchExpressionType = this.switchExpression.typeCheck(initialTypeEnvironment);

        for (Expression caseExpression : caseExpressionList) {
            if (!caseExpression.typeCheck(initialTypeEnvironment).equals(switchExpressionType)) {
                throw new InvalidTypeException("SwitchStatement: expression does not match the type of the switch expression");
            }
        }
        for (Statement caseStatement : caseStatementList) {
            caseStatement.getTypeEnvironment(initialTypeEnvironment.clone());
        }

        return initialTypeEnvironment;
    }
}
