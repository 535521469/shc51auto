package pp.corleone.auto51.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import pp.corleone.auto51.domain.Auto51CarInfo;

public interface Auto51CarInfoDao {

	public Auto51CarInfo get(String seqID);

	public List<Auto51CarInfo> listByUrl(String url);

	public List<Auto51CarInfo> listByUrlAndDeclareDate(String url, Date declareDate);

	public EntityManager getEntityManager();

	public void persist(Auto51CarInfo auto51CarInfo);

	public List<Auto51CarInfo> listByStatusCodeAndLastActiveDateTime(
			int statusCode, Date lastActiveDateTime);

}
