package pp.corleone.auto51.service.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.Log;
import pp.corleone.auto51.dao.Auto51CarInfoDao;
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.RequestWrapper;
import pp.corleone.service.ResponseWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51ListFetcher")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51ListFetcher extends DefaultFetcher {

	@Autowired
	private Auto51CarInfoDao auto51CarInfoDao;

	public Auto51ListFetcher() {
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

	public Auto51ListFetcher(RequestWrapper requestWrapper) {
		super(requestWrapper);
	}

	@Override
	public ResponseWrapper call() throws InterruptedException {
		Log.info(">>> list page :" + this.getRequestWrapper().getUrl());
		return super.call();
	}

}