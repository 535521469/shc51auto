package pp.corleone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultCallback implements Callback {

	private ResponseWrapper responseWrapper;

	public ResponseWrapper getResponseWrapper() {
		return responseWrapper;
	}

	public Callback setResponseWrapper(ResponseWrapper responseWrapper) {
		this.responseWrapper = responseWrapper;
		return this;
	}

	protected final Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass());
	}

}
