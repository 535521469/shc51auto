package pp.corleone.auto51.service.detail.declaredate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.auto51.dao.Auto51CarInfoDao;
import pp.corleone.auto51.dao.Auto51SellerInfoDao;
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
@Component("auto51DetailDeclareDateCallback")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51DetailDeclareDateCallback extends DefaultCallback {

	public Auto51DetailDeclareDateCallback() {
	}

	@Autowired
	private Auto51DetailDeclareDateExtracter auto51DetailDeclareDateExtracter;

	@Autowired
	private Auto51SellerInfoDao auto51SellerInfoDao;
	@Autowired
	private Auto51CarInfoDao auto51CarInfoDao;

	public Auto51DetailDeclareDateExtracter getAuto51DetailDeclareDateExtracter() {
		return this.auto51DetailDeclareDateExtracter;
	}

	@Autowired
	public void setAuto51DetailDeclareDateExtracter(
			Auto51DetailDeclareDateExtracter auto51DetailDeclareDateExtracter) {
		this.auto51DetailDeclareDateExtracter = auto51DetailDeclareDateExtracter;
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

	/**
	 * build shop fetcher
	 * 
	 * @param ele
	 * @return
	 */
	private Fetcher buildSellerFetcher(String shopUrl) {
		Callback sellerCallback = (Callback) Auto51Constant.getInstance()
				.getBean("auto51SellerCallback");

		RequestWrapper referRequestWrapper = this.getResponseWrapper()
				.getReferRequestWrapper();

		RequestWrapper requestWrapper = new DefaultRequestWrapper(shopUrl,
				sellerCallback, this.getResponseWrapper()
						.getReferRequestWrapper(),
				PriorityEnum.SELLER.getValue(), referRequestWrapper.getMeta(),
				referRequestWrapper.getContext());

		DefaultFetcher sellerFetcher = (DefaultFetcher) Auto51Constant
				.getInstance().getBean("auto51SellerFetcher");
		sellerFetcher.setRequestWrapper(requestWrapper);

		return sellerFetcher;
	}

	protected Auto51CarInfo getCarInfoInContext() {
		Auto51CarInfo aci = (Auto51CarInfo) this.getResponseWrapper()
				.getReferRequestWrapper().getContext()
				.get(Auto51Constant.CAR_INFO);
		return aci;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Collection<?>> call() throws Exception {
		Map<String, Collection<?>> fetched = new HashMap<String, Collection<?>>();

		Collection<Fetcher> fetchers = new ArrayList<Fetcher>();
		fetched.put(FetcherConstants.Fetcher, fetchers);

		Document doc = ((DefaultResponseWrapper) this.getResponseWrapper())
				.getDoc();

		Auto51CarInfo auto51CarInfo = this.getCarInfoInContext();
		auto51DetailDeclareDateExtracter.fillDeclareDate(doc, auto51CarInfo);

		if (auto51CarInfo.getAuto51SellerInfo() != null
				&& auto51CarInfo.getAuto51SellerInfo().getShopUrl() != null) {
			Fetcher fetcher = this.buildSellerFetcher(auto51CarInfo
					.getAuto51SellerInfo().getShopUrl());
			fetchers.add(fetcher);
		} else {

			this.auto51CarInfoDao.persist(auto51CarInfo);
			getLogger().info("save ..." + auto51CarInfo.getSeqID());
		}

		return fetched;

	}
}
