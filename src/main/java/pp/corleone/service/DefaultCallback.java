package pp.corleone.service;


public abstract class DefaultCallback implements Callback {

	private ResponseWrapper responseWrapper;

	public ResponseWrapper getResponseWrapper() {
		return responseWrapper;
	}

	public Callback setResponseWrapper(ResponseWrapper responseWrapper) {
		this.responseWrapper = responseWrapper;
		return this;
	}

}
