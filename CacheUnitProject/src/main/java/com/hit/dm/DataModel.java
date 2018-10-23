package com.hit.dm;

import java.io.Serializable;

public class DataModel<T> implements Serializable {
	private long id;
	private T content;

	public DataModel(Long id, T content) {
		this.id = id;
		this.content = content;
	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		DataModel<T> DM = (DataModel<T>) obj;
		if (this.id == DM.getDataModelId()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return ("Id: " + id + "\n" + "Content: " + content + "\n");
	}

	public Long getDataModelId() {
		return this.id;
	}

	public void setDataModelId(Long id) {
		this.id = id;
	}

	public T getContent() {
		return this.content;
	}

	public void setContent(T content) {
		this.content = content;
	}
}
