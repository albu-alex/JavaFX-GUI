package controller;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import model.ADT.*;
import model.Example;
import model.Procedure;
import model.ProgramState;
import model.statement.Statement;
import model.type.Type;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import view.AllExamples;
import view.GUI;

public class GUIController extends Controller {
	private GUI currentGUI;
	
	public GUIController(GUI currentGUI) {
		this.currentGUI = currentGUI;
		AllExamples allExamples = new AllExamples();
		this.exampleList = allExamples.getAllExamples();
	}

	private ProgramState getProgramState(Example currentExample) throws Exception {
		StackInterface<Statement> stack = new MyStack<>();
		StackInterface<DictionaryInterface<String, ValueInterface>> symbolTableStack = new MyStack<>();
		DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<>();
		symbolTableStack.push(symbolTable);
		ListInterface<ValueInterface> output = new MyList<>();
		DictionaryInterface<StringValue, BufferedReader> fileTable = new MyDictionary<>();
		DictionaryInterface<Integer, ValueInterface> heap = new MyHeap<>();
		
		DictionaryInterface<String, Type> typeEnvironment = new MyDictionary<>();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = new MyLockTable<>();
		DictionaryInterface<Integer, Integer> lockTable = new MyLockTable<>();
		DictionaryInterface<Integer, Integer> latchTable = new MyLockTable<>();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = new MyLockTable<>();
		DictionaryInterface<String, Procedure> procedureTable = new MyDictionary<>();
		currentExample.getStatement().getTypeEnvironment(typeEnvironment);
		return new ProgramState(stack, symbolTableStack, output, fileTable, heap,
				semaphoreTable,
				lockTable,
				latchTable,
				barrierTable,
				procedureTable,
				currentExample.getStatement());
	}
	
	@Override
	protected void beforeProgramExecution() {
		super.beforeProgramExecution();
		this.currentGUI.beforeProgramExecution();
	}
	
	public void loadProgramStateIntoRepository(Example currentExample) throws Exception {
		DictionaryInterface<String, Type> typeEnvironment = new MyDictionary<String, Type>();
		currentExample.getStatement().getTypeEnvironment(typeEnvironment);
		this.repository = new Repository(currentExample.getRepositoryLocation());
		this.addProgramState(this.getProgramState(currentExample));
		this.beforeProgramExecution();
	}
	
	public void advanceOneStepAllThreads() throws Exception {
		super.oneStepExecutionAllThreads(this.removeCompletedThreads(this.repository.getThreadList()));
		this.currentGUI.updateAllStructures();
		
		this.repository.setThreadList(this.removeCompletedThreads(this.repository.getThreadList()));
		if (this.repository.getThreadList().size() == 0) {
			this.afterProgramExecution();
		}
	}
	
	@Override
	protected void afterProgramExecution() {
		super.afterProgramExecution();
		this.currentGUI.afterProgramExecution();
	}
	
	@Override
	public void fullProgramExecution() throws Exception {
		if (this.repository == null) {
			// normally this shouldn't happen
			throw new Exception("GUIController: Repository not initialised before calling fullProgramExecution");
		}
		
		List<ProgramState> threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		while (threadsStillInExecution.size() > 0) {
			threadsStillInExecution.get(0).getHeap().setContent(this.getGarbageCollectedHeap(threadsStillInExecution));
			this.advanceOneStepAllThreads();
			threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		}
		
		// afterProgramExecution will be called even when we do a fullexec, because when advanceOneStep finds that it has no threads
		// left, it calls afterProgramExecution - this way, afterProgramExecution is called in both cases (when running one step or the entire program)
	}
}
