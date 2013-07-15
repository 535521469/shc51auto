package pp.corleone.auto51.service.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.service.Callback;
import pp.corleone.service.DefaultCallback;
import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.DefaultRequestWrapper;
import pp.corleone.service.DefaultRequestWrapper.PriorityEnum;
import pp.corleone.service.DefaultResponseWrapper;
import pp.corleone.service.Fetcher;
import pp.corleone.service.FetcherConstants;
import pp.corleone.service.RequestWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51ListCallback")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51ListCallback extends DefaultCallback {

	@Autowired
	private Auto51ListExtracter auto51ListExtracter;

	public Auto51ListExtracter getAuto51ListExtracter() {
		return auto51ListExtracter;
	}

	@Autowired
	public void setAuto51ListExtracter(Auto51ListExtracter auto51ListExtracter) {
		this.auto51ListExtracter = auto51ListExtracter;
	}

	public Auto51ListCallback() {
	}

	protected Fetcher buildFetcher(Auto51CarInfo auto51CarInfo, String detailUrl) {
		Auto51CarInfo aci = new Auto51CarInfo();
		aci.setCityName(auto51CarInfo.getCityName());
		aci.setSellerType(auto51CarInfo.getSellerType());
		aci.setCarSourceUrl(detailUrl);

		Callback auto51DetailCallback = (Callback) Auto51Constant.getInstance()
				.getBean("auto51DetailCallback");

		RequestWrapper requestWrapper = new DefaultRequestWrapper(
				aci.getCarSourceUrl(), auto51DetailCallback, this
						.getResponseWrapper().getReferRequestWrapper(),
				PriorityEnum.DETAIL);
		requestWrapper.getContext().put(Auto51Constant.CAR_INFO, aci);

		DefaultFetcher fetcher = (DefaultFetcher) Auto51Constant.getInstance()
				.getBean("auto51DetailFetcher");
		fetcher.setRequestWrapper(requestWrapper);

		return fetcher;

	}

	protected Auto51CarInfo getCarInfoInContext() {
		Auto51CarInfo aci = (Auto51CarInfo) this.getResponseWrapper()
				.getReferRequestWrapper().getContext()
				.get(Auto51Constant.CAR_INFO);
		return aci;
	}

	protected String getBuildAllPagesInContext() {
		Map<String, Object> ctx = this.getResponseWrapper()
				.getReferRequestWrapper().getContext();
		if (ctx.containsKey(Auto51Constant.BUILD_ALL_PAGE)) {
			return ctx.get(Auto51Constant.BUILD_ALL_PAGE).toString();
		} else {
			return "0";
		}
	}

	protected Fetcher buildNextPageListFetcher(String nextPageUrl,
			Auto51CarInfo auto51CarInfo) {
		Callback callback = (Callback) Auto51Constant.getInstance().getBean(
				"auto51ListCallback");

		RequestWrapper rw = new DefaultRequestWrapper(nextPageUrl, callback,
				this.getResponseWrapper().getReferRequestWrapper(),
				PriorityEnum.LIST);
		rw.getContext().put(Auto51Constant.CAR_INFO, auto51CarInfo);
		DefaultFetcher fetcher = (DefaultFetcher) Auto51Constant.getInstance()
				.getBean("auto51ListFetcher");

		fetcher.setRequestWrapper(rw);
		return fetcher;
	}

	@Override
	public Map<String, Collection<?>> call() throws Exception {

		Map<String, Collection<?>> fetched = new HashMap<String, Collection<?>>();
		Collection<Fetcher> fetchers = new ArrayList<Fetcher>();

		Auto51CarInfo auto51CarInfo = this.getCarInfoInContext();

		Document doc = ((DefaultResponseWrapper) this.getResponseWrapper())
				.getDoc();
		for (String detailUrl : auto51ListExtracter.getDetailUrls(doc)) {
			fetchers.add(this.buildFetcher(auto51CarInfo, detailUrl));
		}

		if (this.getBuildAllPagesInContext() == "1") {
			List<String> nextPageUrls = this.auto51ListExtracter
					.getNextPageUrls(doc);
			nextPageUrls.remove(this.getResponseWrapper().getUrl());
			if (nextPageUrls != null && nextPageUrls.size() > 0) {
				for (String nextPageUrl : nextPageUrls) {
					fetchers.add(buildNextPageListFetcher(nextPageUrl,
							auto51CarInfo));
				}
			}
		}

		fetched.put(FetcherConstants.Fetcher, fetchers);
		return fetched;
	}

}
