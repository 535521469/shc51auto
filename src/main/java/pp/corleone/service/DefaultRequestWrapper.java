package pp.corleone.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("defaultRequestWrapper")
public class DefaultRequestWrapper implements RequestWrapper {

	private Integer timeout;

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	private Callback callback;
	private String url;
	private int priority;
	private Map<MetaEnum, Object> meta = new HashMap<MetaEnum, Object>();
	private Map<String, Object> context = new HashMap<String, Object>();

	public DefaultRequestWrapper() {
	}

	public String getLastRequestUrl() {
		String url = null;
		RequestWrapper requestWrapper = this.getLastReferRequestWrapper();
		if (null != requestWrapper) {
			url = requestWrapper.getUrl();
		}
		return url;
	}

	public RequestWrapper getLastReferRequestWrapper() {
		if (null != this.getReferRequestWrappers()
				&& this.getReferRequestWrappers().size() > 0) {
			return this.getReferRequestWrappers().get(
					this.getReferRequestWrappers().size() - 1);
		}
		return null;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Map<MetaEnum, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<MetaEnum, Object> meta) {
		this.meta = meta;
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public DefaultRequestWrapper(String url) {
		this.setUrl(url);
	}

	public DefaultRequestWrapper(String url, Callback callback) {
		this(url);
		this.setCallback(callback);
	}

	public DefaultRequestWrapper(String url, Callback callback,
			RequestWrapper referRequestWarpper) {
		this(url, callback);
		if (null != referRequestWarpper) {
			this.getReferRequestWrappers().add(referRequestWarpper);
		}
	}

	public DefaultRequestWrapper(String url, Callback callback,
			RequestWrapper referRequestWarpper, int priority) {
		this(url, callback, referRequestWarpper);
		this.setPriority(priority);
	}

	public DefaultRequestWrapper(String url, Callback callback,
			RequestWrapper referRequestWarpper, PriorityEnum priority) {
		this(url, callback, referRequestWarpper, priority.getValue());
	}

	public DefaultRequestWrapper(String url, Callback callback,
			RequestWrapper referRequestWarpper, int priority,
			Map<MetaEnum, Object> metaMap, Map<String, Object> contextMap) {
		this(url, callback, referRequestWarpper, priority);
		if (null != metaMap && !metaMap.isEmpty()) {
			this.setMeta(metaMap);
		}
		if (null != contextMap && !contextMap.isEmpty()) {
			this.setContext(contextMap);
		}
	}

	private List<RequestWrapper> referRequestWrappers = new ArrayList<RequestWrapper>();

	public List<RequestWrapper> getReferRequestWrappers() {
		return referRequestWrappers;
	}

	public void setReferRequestWrappers(
			List<RequestWrapper> referRequestWrappers) {
		this.referRequestWrappers = referRequestWrappers;
	}

	public enum PriorityEnum {

		CHANGE_CITY(100), LIST(50), DETAIL(20), DETAIL_DECLARE_DATE(18), SELLER(
				15), STATUS(10);

		private int value;

		public int getValue() {
			return value;
		}

		private PriorityEnum(int val) {
			this.value = val;
		}
	}

	@Override
	public int getTimeout() {
		if (this.timeout == null) {
			return 3000;
		} else {
			return this.timeout;
		}
	}
}
