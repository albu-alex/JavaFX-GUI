package model;

import java.io.BufferedReader;

import exception.EmptyADTException;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.StackInterface;
import model.statement.Statement;
import model.value.StringValue;
import model.value.ValueInterface;

public class ProgramState {
	private StackInterface<Statement> executionStack;
	private DictionaryInterface<String, ValueInterface> symbolTable;
	private ListInterface<ValueInterface> output;
	private DictionaryInterface<StringValue, BufferedReader> fileTable;
	private DictionaryInterface<Integer, ValueInterface> heap;
	private Statement originalProgram;
	private static int globalThreadCount = 1;
	private final int threadID;
	
	public ProgramState(
			StackInterface<Statement> stack,
			DictionaryInterface<String, ValueInterface> symbolTable, 
			ListInterface<ValueInterface> output,
			DictionaryInterface<StringValue, BufferedReader> fileTable,
			DictionaryInterface<Integer, ValueInterface> heap,
			Statement program
			) {
		this.executionStack = stack;
		this.symbolTable = symbolTable;
		this.output = output;
		this.fileTable = fileTable;
		this.heap = heap;
		//this.originalProgram = program.deepCopy();
		this.setStatement(program);
		this.threadID = ProgramState.manageThreadID();
	}
	
	public static synchronized int manageThreadID() {
		int newThreadID = ProgramState.globalThreadCount;
		ProgramState.globalThreadCount += 1;
		return newThreadID;
	}
	
	public int getThreadID() {
		return this.threadID;
	}
	
	public StackInterface<Statement> getExecutionStack() {
		return this.executionStack;
	}
	
	public DictionaryInterface<String, ValueInterface> getSymbolTable() {
		return this.symbolTable;
	}
	
	public ListInterface<ValueInterface> getOutput() {
		return this.output;
	}
	
	public DictionaryInterface<StringValue, BufferedReader> getFileTable() {
		return this.fileTable;
	}
	
	public DictionaryInterface<Integer, ValueInterface> getHeap() {
		return this.heap;
	}
	
	public Statement getOriginalProgram() {
		return this.originalProgram;
	}
	
	public void setStatement(Statement statement) {
		// we don't add null values to the exeStack because the Deque doesn't accept it
		if (statement != null) {
			this.executionStack.push(statement);
		}
	}
	
	public boolean isCompleted() {
		return (this.executionStack.size() == 0);
	}
	
	public ProgramState oneStepExecution() throws Exception{
		if (this.executionStack.size() == 0) {
			throw new EmptyADTException("ProgramState: No program states available");
		}
		Statement currentStatement = this.executionStack.pop();
		return currentStatement.execute(this);
	}
	
	public String toString() {
		String representation = "";
		
		representation += "\n======== ThreadID: " + Integer.toString(this.threadID) + "========\n";
		representation += "ExecutionStack:\n";
		representation += this.executionStack.toString();
		representation += "\nSymbolTable:\n";
		representation += this.symbolTable.toString();
		representation += "\nFileTable:\n";
		representation += this.fileTable.toString();
		representation += "\nOutputTable:\n";
		representation += this.output.toString();
		representation += "\nHeap:\n";
		representation += this.heap.toString();
		
		return representation;
	}
}
