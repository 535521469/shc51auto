package pp.corleone.auto51.service.seller;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pp.corleone.auto51.dao.Auto51CarInfoDao;
import pp.corleone.auto51.dao.Auto51SellerInfoDao;
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.RequestWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51SellerFetcher")
public class Auto51SellerFetcher extends DefaultFetcher {

	public Auto51SellerFetcher(RequestWrapper requestWrapper) {
		super(requestWrapper);
	}

	private Auto51SellerInfoDao auto51SellerInfoDao;
	private Auto51CarInfoDao auto51CarInfoDao;

	public Auto51SellerInfoDao getAuto51SellerInfoDao() {
		return auto51SellerInfoDao;
	}

	public void setAuto51SellerInfoDao(Auto51SellerInfoDao auto51SellerInfoDao) {
		this.auto51SellerInfoDao = auto51SellerInfoDao;
	}

	public Auto51CarInfoDao getAuto51CarInfoDao() {
		return auto51CarInfoDao;
	}

	public void setAuto51CarInfoDao(Auto51CarInfoDao auto51CarInfoDao) {
		this.auto51CarInfoDao = auto51CarInfoDao;
	}

	public Auto51SellerFetcher() {
	}

	protected Auto51CarInfo getCarInfoInContext() {
		Auto51CarInfo aci = (Auto51CarInfo) this.getRequestWrapper()
				.getContext().get(Auto51Constant.CAR_INFO);
		return aci;
	}

	@Override
	public boolean isIgnore() {
		String url = this.getCarInfoInContext().getCarSourceUrl();
		List<Auto51CarInfo> auto51CarInfos = this.getAuto51CarInfoDao()
				.listByUrl(url);
		if (auto51CarInfos != null && auto51CarInfos.size() > 0) {

			return true;
		} else {
			return false;
		}
	}
}