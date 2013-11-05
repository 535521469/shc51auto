package pp.corleone.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.Log;
import pp.corleone.auto51.dao.HttpProxyDao;
import pp.corleone.domain.HttpProxy;

@Component("proxyGetter")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DefaultProxyGetter implements ProxyGetter {

	private Iterator<HttpProxy> httpProxies = Collections.emptyIterator();

	@Autowired
	private HttpProxyDao httpProxyDao;

	public HttpProxyDao getHttpProxyDao() {
		return httpProxyDao;
	}

	@Autowired
	public void setHttpProxyDao(HttpProxyDao httpProxyDao) {
		this.httpProxyDao = httpProxyDao;
	}

	@Override
	public HttpProxy get() {

		if (this.httpProxies.hasNext()) {
			return this.httpProxies.next();
		} else {
			Collection<HttpProxy> hps = httpProxyDao.getHttpProxies();
			Log.info("get " + hps.size() + " proxies");
			httpProxies = hps.iterator();
			return this.get();
		}
	}
}
