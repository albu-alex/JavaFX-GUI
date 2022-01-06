package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.Type;

public interface Statement {
	public ProgramState execute(ProgramState crtState) throws Exception;
	public DictionaryInterface<String, Type> getTypeEnvironment(DictionaryInterface<String, Type> initialTypeEnvironment) throws Exception;
	public String toString();
}
