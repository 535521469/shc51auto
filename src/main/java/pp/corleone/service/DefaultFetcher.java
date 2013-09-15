package pp.corleone.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import pp.corleone.Log;

public abstract class DefaultFetcher implements Fetcher {

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

	@Override
	public ResponseWrapper call() throws InterruptedException {

		DefaultResponseWrapper rw = null;
		if (!this.isIgnore()) {
			String url = this.getRequestWrapper().getUrl();
			Log.debug("crawl " + url);
			Document doc;
			try {
				doc = Jsoup
						.connect(url)
						.timeout(this.getRequestWrapper().getTimeout())
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36")
						.get();
				rw = new DefaultResponseWrapper(doc, this.requestWrapper);
			} catch (Exception e) {
				Log.error("error url :" + url + ",fetcher:"
						+ this.getClass().getName(), e);
			}

		}
		return rw;
	}

}
