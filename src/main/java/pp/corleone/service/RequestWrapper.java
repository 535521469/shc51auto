package pp.corleone.service;

import java.util.List;
import java.util.Map;

public interface RequestWrapper {

	public enum MetaEnum {
		RETRY_TIMES,MAX_RETRY_TIMES,
	}

	public abstract int getTimeout();

	public abstract String getUrl();

	public abstract Callback getCallback();

	public abstract List<RequestWrapper> getReferRequestWrappers();

	public abstract Map<MetaEnum, Object> getMeta();

	public abstract Map<String, Object> getContext();

	public abstract int getPriority();

	public abstract String getLastRequestUrl();

}
