package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.Type;

public interface Statement {
	ProgramState execute(ProgramState crtState) throws Exception;
	DictionaryInterface<String, Type> getTypeEnvironment(DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception;
	String toString();
}
