package com.ja0ck5.thrift;

import org.apache.commons.io.IOUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ThriftClientProxy {

	protected TTransport transport;
	protected TProtocol protocol;
	
	public ThriftClientProxy(String server_url, int port)
			throws TTransportException {
		init(server_url, port);
	}

	/**
	 * @param server_url
	 * @param port
	 * @throws TTransportException
	 */
	private void init(String server_url, int port) throws TTransportException {
		transport = new TSocket(server_url, port);
		protocol = new TBinaryProtocol(new TFramedTransport(transport));
		transport.open();
	}
	
	public void close() {
		IOUtils.closeQuietly(transport);
	}

	public boolean isOpen() {
		return transport.isOpen();
	}

	/**
	 * @return the {@link #protocol}
	 */
	public TProtocol getProtocol() {
		return protocol;
	}

	public TTransport getTransport() {
		return transport;
	}
	
	
}
