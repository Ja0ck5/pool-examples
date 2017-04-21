package com.ja0ck5.thrift.i;

import org.apache.thrift.transport.TTransportException;

import com.ja0ck5.thrift.ThriftClientProxy;

public interface IThriftClientProxy {
	ThriftClientProxy createThriftClientProxy() throws TTransportException;
}
