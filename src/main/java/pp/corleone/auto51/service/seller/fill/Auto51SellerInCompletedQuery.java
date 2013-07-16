package pp.corleone.auto51.service.seller.fill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.auto51.dao.Auto51SellerInfoDao;
import pp.corleone.auto51.domain.Auto51SellerInfo;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51SellerInCompletedQuery")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51SellerInCompletedQuery {

	@Autowired
	private Auto51SellerInfoDao auto51SellerInfoDao;

	public Auto51SellerInfoDao getAuto51SellerInfoDao() {
		return auto51SellerInfoDao;
	}

	@Autowired
	public void setAuto51SellerInfoDao(Auto51SellerInfoDao auto51SellerInfoDao) {
		this.auto51SellerInfoDao = auto51SellerInfoDao;
	}

	public List<Auto51SellerInfo> listIncompletedSellers() {
		return this.auto51SellerInfoDao.listIncompletedSellers();
	}

}
