package com.ja0ck5.pool.factory;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ja0ck5.thrift.ThriftClientProxy;
/**
 * 
 * @author Jack
 *
 */
public class ThriftClientProxyFactory implements PooledObjectFactory<ThriftClientProxy>{
	
	private static final Logger log = LoggerFactory.getLogger(ThriftClientProxyFactory.class);
	
	private final AtomicReference<ThriftClientProxy> thriftClientProxy = new AtomicReference<ThriftClientProxy>();
	
	private String server_url;
	private int port;
	
	public ThriftClientProxyFactory(String server_url, int port)
			throws TTransportException {
		this.server_url = server_url;
		this.port = port;
		this.thriftClientProxy.set(new ThriftClientProxy(server_url, port));
	}

	@Override
	public PooledObject<ThriftClientProxy> makeObject() throws Exception {
	    final ThriftClientProxy thriftClientProxy = this.thriftClientProxy.get();
	    return new DefaultPooledObject<ThriftClientProxy>(thriftClientProxy);
	}

	@Override
	public void destroyObject(PooledObject<ThriftClientProxy> p) throws Exception {
		final ThriftClientProxy thriftClientProxy = p.getObject();
		try {
			if(thriftClientProxy.isOpen()){
				if(log.isDebugEnabled()){
					log.debug("destroyed object...");
				}
				thriftClientProxy.close();
			}
		} catch (Exception e) {
			log.error("Could not destroy object");
		}
	}

	@Override
	public boolean validateObject(PooledObject<ThriftClientProxy> p) {
		return false;
	}

	@Override
	public void activateObject(PooledObject<ThriftClientProxy> p) throws Exception {
		log.info("active object...");
		final ThriftClientProxy thriftClientProxy = p.getObject();
		TTransport transport = thriftClientProxy.getTransport();
		if(transport != null){
			transport.close();
		}
		//FIXME temporary
		for(int i=0;;i++){
			try {
				this.thriftClientProxy.set(new ThriftClientProxy(server_url, port));
				break;
			} catch (Exception e) {
				log.debug("reconnect error. {}", e);
				if(3 == i){
					throw e;
				}
			}
		}
	}

	@Override
	public void passivateObject(PooledObject<ThriftClientProxy> p) throws Exception {
		/*final ThriftClientProxy thriftClientProxy = p.getObject();
		thriftClientProxy.getProtocol().reset();*/
	}


}
