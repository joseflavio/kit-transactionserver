package com.kit.lightserver.adapters.adapterout;

import kit.primitives.base.Primitive;

final class AdoConverterResult<T extends Primitive> {

	private final boolean success;

	private final T primitiveToSend;

	public AdoConverterResult(final boolean success, final T primitiveToSend) {
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
