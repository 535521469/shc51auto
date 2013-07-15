package pp.corleone.service;

import java.util.concurrent.Callable;

public interface Fetcher extends Callable<ResponseWrapper> {

	public abstract RequestWrapper getRequestWrapper();

	public abstract boolean isIgnore() throws InterruptedException;

}