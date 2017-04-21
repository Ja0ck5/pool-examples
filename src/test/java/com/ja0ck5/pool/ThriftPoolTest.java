package com.ja0ck5.pool;


import java.io.IOException;
import java.util.Set;

import org.apache.commons.pool2.impl.DefaultPooledObjectInfo;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.Test;

import com.ja0ck5.pool.factory.ThriftClientProxyFactory;
import com.ja0ck5.thrift.ThriftClientProxy;

public class ThriftPoolTest {
	
	String thrift_server_url = "14.17.108.171";
	int thrift_server_prot = 9009;
	GenericObjectPoolConfig poolConfig;
	BasePool<ThriftClientProxy> basePool;
	ThriftPool thriftPool;
	
	@Before
	public void setUp() throws Exception {
		poolConfig =new GenericObjectPoolConfig();
		poolConfig.setMinIdle(5000);
		poolConfig.setMaxWaitMillis(5000);
		
		basePool = new BasePool<ThriftClientProxy>(poolConfig, new ThriftClientProxyFactory(thrift_server_url, thrift_server_prot));
		thriftPool = new ThriftPool(poolConfig, new ThriftClientProxyFactory(thrift_server_url, thrift_server_prot));
	}

	@Test
	public void testThriftPool() {
		ThriftClientProxy resource = null;
		try {
			try {
				basePool = new BasePool<ThriftClientProxy>(poolConfig, new ThriftClientProxyFactory(thrift_server_url, thrift_server_prot));
			} catch (TTransportException e) {
				e.printStackTrace();
			}
			resource = (ThriftClientProxy) basePool.getResource();
			TProtocol protocol = resource.getProtocol();
			System.out.println(protocol.toString());
		}finally {
			if(basePool != null){
				basePool.returnObject(resource);
			}
		}
	}

	@Test
	public void testListAllObjects() throws TTransportException {
		ThriftClientProxy resource = null;
		try {
			resource = thriftPool.getResource();
			Set<DefaultPooledObjectInfo> listAllObjects = thriftPool.listAllObjects();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (DefaultPooledObjectInfo info : listAllObjects) {
				System.out.println("LastReturnTime = " + info.getLastReturnTime()+ ", CreateTime = " +info.getCreateTime());
				System.out.println("Time Step = " + (info.getLastReturnTime()- info.getCreateTime()));
			}
		}finally {
			if(thriftPool != null){
				thriftPool.returnObject(resource);
			}
		}
	}

	@Test
	public void testClose() throws TTransportException {
		ThriftClientProxy resource = null;
		try {
			resource = thriftPool.getResource();
			try {
				thriftPool.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}finally {
			if(thriftPool != null){
				thriftPool.returnObject(resource);
			}
		}
	}

}
