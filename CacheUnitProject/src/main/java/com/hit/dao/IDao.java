package com.hit.dao;
import java.io.Serializable;

import com.hit.dm.DataModel;

public interface IDao <ID extends Serializable, T>{
	public void save(T entity);
	public void delete(T entity);
	public T find(ID id);
	public int countAll();
}
