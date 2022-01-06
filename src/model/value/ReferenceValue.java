package model.value;

import model.type.ReferenceType;
import model.type.Type;

public class ReferenceValue implements ValueInterface{
	private final int heapAddress;
	public static final int DEFAULT_HEAP_ADDRESS = 0;
	private final Type referencedType;
	
	public ReferenceValue(int heapAddress, Type referencedType) {
		this.heapAddress = heapAddress;
		this.referencedType = referencedType;
	}
	
	public ReferenceValue(Type referencedType) {
		this.heapAddress = ReferenceValue.DEFAULT_HEAP_ADDRESS;
		this.referencedType = referencedType;
	}
	
	@Override
	public boolean equals(Object another) {
		return (another instanceof ReferenceValue && ((ReferenceValue)another).getHeapAddress() == this.heapAddress);
	}
	
	public int getHeapAddress() {
		return this.heapAddress;
	}
	
	public Type getReferencedType() {
		return this.referencedType;
	}
	
	@Override
	public Type getType() {
		return new ReferenceType(this.referencedType);
	}
	
	public String toString() {
		String representation = "";
		representation += ("(0x" + Integer.toHexString(this.heapAddress) + ", " + this.referencedType.toString() + ")");
		return representation;
	}
}
