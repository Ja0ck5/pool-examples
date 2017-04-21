package com.ja0ck5.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.transport.TTransportException;

import com.ja0ck5.thrift.ThriftClientProxy;

public class ThriftPool extends BasePool<ThriftClientProxy> {

	public ThriftPool(GenericObjectPoolConfig poolConfig, PooledObjectFactory<ThriftClientProxy> factory)
			throws TTransportException {
		super(poolConfig, factory);
	}

}
