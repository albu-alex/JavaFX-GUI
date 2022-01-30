package model.statement;

import javafx.util.Pair;
import model.ADT.MyLockTable;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.type.Type;
import model.value.ValueInterface;

import java.util.ArrayList;

public class ForkStatement implements Statement {
	private final Statement threadStatements;
	
	public ForkStatement(Statement threadInstructions) {
		this.threadStatements = threadInstructions;
	}
	
	@Override
	public ProgramState execute(ProgramState parentThread) throws Exception {
		if (this.threadStatements == null) {
			return null;
		}
		
		StackInterface<Statement> stack = new MyStack<>();
		DictionaryInterface<String, ValueInterface> symbolTable = parentThread.getSymbolTable().clone();
		return new ProgramState(stack, symbolTable, parentThread.getOutput(), parentThread.getFileTable(), parentThread.getHeap(),
				parentThread.getSemaphoreTable(),
				parentThread.getLockTable(),
				parentThread.getLatchTable(),
				parentThread.getBarrierTable(),
				this.threadStatements);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("fork\n(\n" + this.threadStatements.toString() + ")\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		this.threadStatements.getTypeEnvironment(initialTypeEnvironment.clone());
		return initialTypeEnvironment;
	}
}
