package pp.corleone.auto51.service.seller;

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

import pp.corleone.Log;
import pp.corleone.auto51.dao.Auto51CarInfoDao;
import pp.corleone.auto51.dao.Auto51SellerInfoDao;
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51SellerInfo;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.service.DefaultCallback;
import pp.corleone.service.DefaultResponseWrapper;
import pp.corleone.service.Fetcher;
import pp.corleone.service.FetcherConstants;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51SellerCallback")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51SellerCallback extends DefaultCallback {

	@Autowired
	private Auto51SellerExtracter auto51SellerExtracter;

	public Auto51SellerExtracter getAuto51SellerExtracter() {
		return auto51SellerExtracter;
	}

	@Autowired
	public void setAuto51SellerExtracter(
			Auto51SellerExtracter auto51SellerExtracter) {
		this.auto51SellerExtracter = auto51SellerExtracter;
	}

	public Auto51SellerCallback() {
	}

	@Autowired
	private Auto51CarInfoDao auto51CarInfoDao;
	@Autowired
	private Auto51SellerInfoDao auto51SellerInfoDao;

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

		Auto51SellerInfo auto51SellerInfo = auto51CarInfo.getAuto51SellerInfo();
		if (auto51SellerInfo != null && auto51SellerInfo.getShopName() != null) {
			Auto51SellerInfo existAuto51SellerInfo = this
					.getAuto51SellerInfoDao().listByName(
							auto51SellerInfo.getShopName());
			if (existAuto51SellerInfo != null) {
				auto51CarInfo.setAuto51SellerInfo(existAuto51SellerInfo);
			}
		}
		this.auto51SellerExtracter.fillSellerInfo(doc,
				auto51CarInfo.getAuto51SellerInfo());

		this.auto51CarInfoDao.persist(auto51CarInfo);
		Log.info(
				"save shop type car info..." + auto51CarInfo.getSeqID());

		return fetched;

	}

}
