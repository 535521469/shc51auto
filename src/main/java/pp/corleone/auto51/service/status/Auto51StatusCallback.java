package pp.corleone.auto51.service.status;

import java.util.Collection;
import java.util.Date;
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
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51CarInfo.Auto51StatusCode;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.auto51.service.detail.Auto51DetailExtracter;
import pp.corleone.service.DefaultCallback;
import pp.corleone.service.DefaultResponseWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51StatusCallback")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51StatusCallback extends DefaultCallback {

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
	private Auto51CarInfoDao auto51CarInfoDao;

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
		Auto51CarInfo auto51CarInfo = this.getCarInfoInContext();
		Document doc = ((DefaultResponseWrapper) this.getResponseWrapper())
				.getDoc();

		boolean isOnline = auto51DetailExtracter.isOnline(doc);

		Auto51CarInfo car = this.getAuto51CarInfoDao().get(
				auto51CarInfo.getSeqID());

		if (isOnline) {
			car.setLastActiveDate(new Date());
			Log.info(car.getSeqID()+" online");
		} else {
			car.setStatusType(Auto51StatusCode.STATUS_TYPE_SOLD);
			car.setOfflineDate(new Date());
			Log.info(car.getSeqID()+" offline");
		}

		return null;
	}

}
