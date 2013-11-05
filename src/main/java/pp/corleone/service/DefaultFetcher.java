package pp.corleone.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import pp.corleone.Log;
import pp.corleone.domain.HttpProxy;
import pp.corleone.service.RequestWrapper.MetaEnum;

public abstract class DefaultFetcher implements Fetcher {

	@Autowired
	private ProxyGetter proxyGetter;

	public ProxyGetter getProxyGetter() {
		return proxyGetter;
	}

	@Autowired
	public void setProxyGetter(ProxyGetter proxyGetter) {
		this.proxyGetter = proxyGetter;
	}

	private RequestWrapper requestWrapper;

	public DefaultFetcher() {
	}

	public DefaultFetcher(RequestWrapper requestWrapper) {
		this.setRequestWrapper(requestWrapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pp.corleone.service.Fetcher#getRequestWrapper()
	 */
	@Override
	public RequestWrapper getRequestWrapper() {
		return requestWrapper;
	}

	public void setRequestWrapper(RequestWrapper requestWrapper) {
		this.requestWrapper = requestWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pp.corleone.service.Fetcher#isIgnore()
	 */
	@Override
	public boolean isIgnore() throws InterruptedException {
		return false;
	}

	private ResponseWrapper fetch() throws InterruptedException {

		DefaultResponseWrapper rw = null;
		if (!this.isIgnore()) {
			String url = this.getRequestWrapper().getUrl();
			Log.debug("crawl " + url);
			Document doc;
			HttpProxy hp = proxyGetter.get();
			try {
				// doc = Jsoup
				// .connect(url)
				// .timeout(this.getRequestWrapper().getTimeout())
				// .userAgent(
				// "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36")
				// .get();

				Log.debug("use:" + hp.expr() + " crawle " + url);

				URL fetchUrl = new URL(url);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
						hp.getIp(), hp.getPort())); // or whatever your proxy is
				HttpURLConnection uc = (HttpURLConnection) fetchUrl
						.openConnection(proxy);

				uc.setRequestProperty(
						"User-agent",
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36");

				uc.setConnectTimeout(2000);
				uc.setReadTimeout(2000);

				uc.connect();

				String line = null;
				StringBuffer tmp = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						uc.getInputStream(), "gbk"));
				while ((line = in.readLine()) != null) {
					tmp.append(line);
				}

				doc = Jsoup.parse(String.valueOf(tmp));
				doc.setBaseUri(url);

				rw = new DefaultResponseWrapper(doc, this.requestWrapper);
			} catch (Exception e) {

				if (e instanceof InterruptedException) {
					throw ((InterruptedException) e);
				} else {
					if (requestWrapper.getMeta().get(MetaEnum.RETRY_TIMES) == null) {
						requestWrapper.getMeta().put(MetaEnum.RETRY_TIMES, 0);
					}

					if (((Integer) requestWrapper.getMeta().get(
							MetaEnum.RETRY_TIMES)) < 8) {
						requestWrapper.getMeta().put(
								MetaEnum.RETRY_TIMES,
								1 + ((Integer) requestWrapper.getMeta().get(
										MetaEnum.RETRY_TIMES)));
						Log.info("retry for "
								+ requestWrapper.getMeta().get(
										MetaEnum.RETRY_TIMES) + " times," + url);
						return this.fetch();
					} else if (((Integer) requestWrapper.getMeta().get(
							MetaEnum.RETRY_TIMES)) >= 8) {
						Log.info("give up retry :" + url);
					}
				}
			}
		}
		return rw;
	}

	@Override
	public ResponseWrapper call() throws InterruptedException {
		ResponseWrapper rw = null;
		rw = this.fetch();
		return rw;

	}
}
