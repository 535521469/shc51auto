package pp.corleone.auto51.service.detail.declaredate;

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
import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.RequestWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51DetailDeclareDateFetcher")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51DetailDeclareDateFetcher extends DefaultFetcher {

	public Auto51DetailDeclareDateFetcher(RequestWrapper requestWrapper) {
		super(requestWrapper);
	}

	@Autowired
	private Auto51SellerInfoDao auto51SellerInfoDao;
	@Autowired
	private Auto51CarInfoDao auto51CarInfoDao;

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
		Auto51CarInfo aci = (Auto51CarInfo) this.getRequestWrapper()
				.getContext().get(Auto51Constant.CAR_INFO);
		return aci;
	}

	public Auto51DetailDeclareDateFetcher() {
	}

}
