package pp.corleone.auto51.service.seller.fill;

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
import pp.corleone.auto51.dao.Auto51SellerInfoDao;
import pp.corleone.auto51.domain.Auto51SellerInfo;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.auto51.service.seller.Auto51SellerExtracter;
import pp.corleone.service.DefaultCallback;
import pp.corleone.service.DefaultResponseWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51SellerFillCallback")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51SellerFillCallback extends DefaultCallback {

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

	public Auto51SellerFillCallback() {
	}

	@Autowired
	private Auto51SellerInfoDao auto51SellerInfoDao;

	public Auto51SellerInfoDao getAuto51SellerInfoDao() {
		return auto51SellerInfoDao;
	}

	@Autowired
	public void setAuto51SellerInfoDao(Auto51SellerInfoDao auto51SellerInfoDao) {
		this.auto51SellerInfoDao = auto51SellerInfoDao;
	}

	protected Auto51SellerInfo getSellerInfoInContext() {
		Auto51SellerInfo aci = (Auto51SellerInfo) this.getResponseWrapper()
				.getReferRequestWrapper().getContext()
				.get(Auto51Constant.SELLER_INFO);
		return aci;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Collection<?>> call() throws Exception {

		Map<String, Collection<?>> fetched = new HashMap<String, Collection<?>>();
		Document doc = ((DefaultResponseWrapper) this.getResponseWrapper())
				.getDoc();

		Auto51SellerInfo auto51SellerInfo = this.getSellerInfoInContext();

		Auto51SellerInfo existSeller = auto51SellerInfoDao.get(auto51SellerInfo
				.getSeqID());

		if (existSeller != null) {
			this.auto51SellerExtracter.fillSellerInfo(doc, existSeller);
		}

		Log.info(
				"fill selelr info..." + existSeller.getSeqID() + ","
						+ existSeller.getShopName());

		return fetched;

	}

}
