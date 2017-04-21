package com.ja0ck5.pool;

import java.io.Closeable;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObjectInfo;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.transport.TTransportException;

public class BasePool<T> implements Closeable {
	protected GenericObjectPool<T> internalPool;

	public BasePool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory)
			throws TTransportException {
		initPool(poolConfig, factory);
	}

	public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {
		if (this.internalPool != null) {
			try {
				close();
			} catch (Exception e) {
			}
		}
		this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
	}

	public T getResource() {
		try {
			return internalPool.borrowObject();
		} catch (Exception e) {
			throw new NoSuchElementException("Conld not get a resource from pool");
		}
	}

	public Set<DefaultPooledObjectInfo> listAllObjects() {
		try {
			return internalPool.listAllObjects();
		} catch (Exception e) {
			throw new NoSuchElementException("Conld not get all resources from pool");
		}
	}

	public void returnObject(T obj){
		try {
			this.internalPool.returnObject(obj);
		} catch (Exception e) {
	        throw new RuntimeException("Could not return the resource to the pool", e);
		}
	}
	
	@Override
	public void close() throws IOException {
		try {
			internalPool.close();
		} catch (Exception e) {
			throw new RuntimeException("Could not destroy the pool", e);
		}
	}

}
