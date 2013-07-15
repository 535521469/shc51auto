package pp.corleone.auto51.service.status;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.auto51.dao.Auto51CarInfoDao;
import pp.corleone.auto51.domain.Auto51CarInfo;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51OnlineQuery")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51OnlineQuery {

	@Autowired
	private Auto51CarInfoDao auto51CarInfoDao;

	public Auto51CarInfoDao getAuto51CarInfoDao() {
		return auto51CarInfoDao;
	}

	@Autowired
	public void setAuto51CarInfoDao(Auto51CarInfoDao auto51CarInfoDao) {
		this.auto51CarInfoDao = auto51CarInfoDao;
	}

	public List<Auto51CarInfo> listByStatusCodeAndLastActiveDateTime(
			int statusCode, Date lastActiveDateTime) {
		return this.getAuto51CarInfoDao()
				.listByStatusCodeAndLastActiveDateTime(statusCode,
						lastActiveDateTime);
	}

	public Auto51CarInfo get(String seqID) {
		return this.getAuto51CarInfoDao().get(seqID);
	}
}
