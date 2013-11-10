package pp.corleone.auto51.service.detail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pp.corleone.Log;
import pp.corleone.auto51.dao.Auto51CarInfoDao;
import pp.corleone.auto51.dao.Auto51SellerInfoDao;
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51CarInfo.Auto51SellerType;
import pp.corleone.auto51.domain.Auto51SellerInfo;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.service.DefaultCallback;
import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.DefaultRequestWrapper;
import pp.corleone.service.DefaultRequestWrapper.PriorityEnum;
import pp.corleone.service.DefaultResponseWrapper;
import pp.corleone.service.Fetcher;
import pp.corleone.service.FetcherConstants;
import pp.corleone.service.RequestWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51DetailCallback")
public class Auto51DetailCallback extends DefaultCallback {

	public Auto51DetailCallback() {
	}

	@Autowired
	private Auto51DetailExtracter auto51DetailExtracter;

	public Auto51DetailExtracter getAuto51DetailExtracter() {
		return auto51DetailExtracter;
	}

	@Autowired
	public void setAuto51DetailExtracter(
			Auto51DetailExtracter auto51DetailExtracter) {
		this.auto51DetailExtracter = auto51DetailExtracter;
	}

	@Autowired
	private Auto51DetailDeclareDateUrlBuilder auto51DetailDeclareDateUrlBuilder;
	@Autowired
	private Auto51SellerInfoDao auto51SellerInfoDao;
	@Autowired
	private Auto51CarInfoDao auto51CarInfoDao;

	public Auto51DetailDeclareDateUrlBuilder getAuto51DetailDeclareDateUrlBuilder() {
		return auto51DetailDeclareDateUrlBuilder;
	}

	@Autowired
	public void setAuto51DetailDeclareDateUrlBuilder(
			Auto51DetailDeclareDateUrlBuilder auto51DetailDeclareDateUrlBuilder) {
		this.auto51DetailDeclareDateUrlBuilder = auto51DetailDeclareDateUrlBuilder;
	}

	public Auto51SellerInfoDao getAuto51SellerInfoDao() {
		return auto51SellerInfoDao;
	}

	@Autowired
	public void setAuto51SellerInfoDao(Auto51SellerInfoDao auto51SellerInfoDao) {
		this.auto51SellerInfoDao = auto51SellerInfoDao;
	}

	public Auto51CarInfoDao getAuto51CarInfoDao() {
		return auto51CarInfoDao;
	}

	@Autowired
	public void setAuto51CarInfoDao(Auto51CarInfoDao auto51CarInfoDao) {
		this.auto51CarInfoDao = auto51CarInfoDao;
	}

	private void copyCarValues(Auto51CarInfo target, Auto51CarInfo src) {
		target.setCarSourceUrl(src.getCarSourceUrl());
		target.setCityName(src.getCityName());
		target.setSellerType(src.getSellerType());
	}

	protected Auto51CarInfo getCarInfoInContext() {
		Auto51CarInfo aci = (Auto51CarInfo) this.getResponseWrapper()
				.getReferRequestWrapper().getContext()
				.get(Auto51Constant.CAR_INFO);
		return aci;
	}

	protected Fetcher buildFetcher(Auto51CarInfo auto51CarInfo,
			String declareDateUrl) {

		DefaultCallback auto51DetailDeclareDateCallback = (DefaultCallback) Auto51Constant
				.getInstance().getBean("auto51DetailDeclareDateCallback");

		RequestWrapper requestWrapper = new DefaultRequestWrapper(
				declareDateUrl, auto51DetailDeclareDateCallback, this
						.getResponseWrapper().getReferRequestWrapper(),
				PriorityEnum.DETAIL_DECLARE_DATE);
		requestWrapper.getContext().put(Auto51Constant.CAR_INFO, auto51CarInfo);

		DefaultFetcher defaultFetcher = (DefaultFetcher) Auto51Constant
				.getInstance().getBean("auto51DetailDeclareDateFetcher");
		defaultFetcher.setRequestWrapper(requestWrapper);

		return defaultFetcher;
	}

	@Override
	public Map<String, Collection<?>> call() throws Exception {
		Map<String, Collection<?>> fetched = new HashMap<String, Collection<?>>();
		Collection<Fetcher> fetchers = new ArrayList<Fetcher>();
		fetched.put(FetcherConstants.Fetcher, fetchers);

		Auto51CarInfo aci = this.getCarInfoInContext();

		Document doc = ((DefaultResponseWrapper) this.getResponseWrapper())
				.getDoc();

		Auto51CarInfo auto51CarInfo;
		try {
			auto51CarInfo = auto51DetailExtracter.getCarInfo(doc);

			// Log.info(doc.toString());

			if (StringUtils.isBlank(auto51CarInfo.getPrice())) {
				throw new IllegalArgumentException("price is None -"
						+ this.getResponseWrapper().getUrl());
			}

		} catch (Exception e) {
			Log.error("extract detail error,"
					+ this.getResponseWrapper().getUrl());
			Log.error("", e);
			throw e;
		}
		this.copyCarValues(auto51CarInfo, aci);

		if (auto51CarInfo.getSellerType() == Auto51SellerType.SHOP.getCode()) {
			Auto51SellerInfo auto51SellerInfo = auto51CarInfo
					.getAuto51SellerInfo();
			if (auto51SellerInfo != null
					&& auto51SellerInfo.getShopName() != null) {
				Auto51SellerInfo existAuto51SellerInfo = this
						.getAuto51SellerInfoDao().listByName(
								auto51SellerInfo.getShopName());
				if (existAuto51SellerInfo != null) {
					auto51CarInfo.setAuto51SellerInfo(existAuto51SellerInfo);
				}
			} else {
				Log.error("shop seller type has no seller info,"
						+ auto51CarInfo.getCarSourceUrl());
				// auto51CarInfo=auto51DetailExtracter.getCarInfo(doc);
			}
		}
		String declareDateUrl = auto51DetailDeclareDateUrlBuilder
				.getCarDeclareDateUrl(auto51CarInfo, doc);

		Fetcher fetcher = this.buildFetcher(auto51CarInfo, declareDateUrl);
		fetchers.add(fetcher);

		return fetched;

	}
}
