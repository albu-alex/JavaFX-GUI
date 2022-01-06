package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.Type;

public class EmptyStatement implements Statement {

	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		return null;
	}
	
	public String toString() {
		String representation = "";
		representation += "empty_statement\n";
		return representation;
	}

	@Override
	public DictionaryInterface<String, Type> getTypeEnvironment(
			DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception {
		return initialTypeEnvironment;
	}

}
