package view;

import java.io.BufferedReader;
import java.util.ArrayList;

import controller.TextController;
import controller.Controller;
import javafx.util.Pair;
import model.ADT.*;
import model.Example;
import model.ProgramState;
import model.statement.Statement;
import model.type.Type;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import repository.RepositoryInterface;

public class RunExampleCommand extends Command {
	private final Statement crtStatement;
	private final String repositoryLocation;
	
	public RunExampleCommand(String key, Example exampleToRun) {
		super(key, exampleToRun.getExampleDescription());
		this.crtStatement = exampleToRun.getStatement();
		this.repositoryLocation = exampleToRun.getRepositoryLocation();
	}
	
	@Override
	public void execute() throws Exception {
		StackInterface<Statement> stack = new MyStack<>();
		DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<>();
		ListInterface<ValueInterface> output = new MyList<>();
		DictionaryInterface<StringValue, BufferedReader> fileTable = new MyDictionary<>();
		DictionaryInterface<Integer, ValueInterface> heap = new MyHeap<>();
		
		DictionaryInterface<String, Type> typeEnvironment = new MyDictionary<>();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = new MyLockTable<>();
		DictionaryInterface<Integer, Integer> lockTable = new MyLockTable<>();
		DictionaryInterface<Integer, Integer> latchTable = new MyLockTable<>();
		this.crtStatement.getTypeEnvironment(typeEnvironment);
		ProgramState crtProgramState = new ProgramState(stack, symbolTable, output, fileTable, heap,
				semaphoreTable,
				lockTable,
				latchTable,
				this.crtStatement);
		
		RepositoryInterface repo = new Repository(this.repositoryLocation);
		Controller controller = new TextController(repo);
		controller.addProgramState(crtProgramState);
		controller.fullProgramExecution();
	}
}
