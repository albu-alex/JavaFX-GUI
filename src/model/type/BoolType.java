package model.type;

import model.value.BoolValue;
import model.value.ValueInterface;

public class BoolType implements Type {
	@Override
	public boolean equals(Object another) {
		return (another instanceof BoolType);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += "bool";
		return representation;
	}

	@Override
	public ValueInterface getDefaultValue() {
		return new BoolValue(); // the default constructor sets the value to the default one
	}
}
