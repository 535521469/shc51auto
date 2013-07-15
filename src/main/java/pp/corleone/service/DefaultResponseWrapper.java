package pp.corleone.service;

import java.util.Map;

import org.jsoup.nodes.Document;

import pp.corleone.service.RequestWrapper.MetaEnum;

public class DefaultResponseWrapper implements ResponseWrapper {

	private Document doc;
	private RequestWrapper referRequestWrapper;

	public String getUrl() {
		return this.getReferRequestWrapper().getUrl();
	}

	public Map<MetaEnum, Object> getMeta() {
		return this.getReferRequestWrapper().getMeta();
	}

	public Map<String, Object> getContext() {
		return this.getReferRequestWrapper().getContext();
	}

	public RequestWrapper getReferRequestWrapper() {
		return referRequestWrapper;
	}

	public void setReferRequestWrapper(RequestWrapper referRequestWrapper) {
		this.referRequestWrapper = referRequestWrapper;
		// this.referRequestWrapper.getCallback().setResponseWrapper(this);
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public DefaultResponseWrapper() {
	}

	public DefaultResponseWrapper(Document doc,
			RequestWrapper referRequestWrapper) {
		this.setDoc(doc);
		this.setReferRequestWrapper(referRequestWrapper);
	}
}
