package com.kit.lightserver.adapterout;

import kit.primitives.base.Primitive;

final class AdapterOutConverterResult<T extends Primitive> {

	private final boolean success;

	private final T primitiveToSend;

	public AdapterOutConverterResult(final boolean success, final T primitiveToSend) {
		this.success = success;
		this.primitiveToSend = primitiveToSend;
	}// constructor

	public boolean isSuccess() {
		return success;
	}

	public Primitive getPrimitiveToSend() {
		return primitiveToSend;
	}

	@Override
	public String toString() {
		return "ConverterResult [success=" + success + ", primitiveToSend=" + primitiveToSend + "]";
	}

}// class
