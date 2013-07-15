package pp.corleone.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

public interface Callback extends Callable<Map<String, Collection<?>>> {

	Callback setResponseWrapper(ResponseWrapper responseWrapper);

	ResponseWrapper getResponseWrapper();
}
