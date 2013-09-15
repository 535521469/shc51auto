package pp.corleone.auto51.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import pp.corleone.auto51.domain.Auto51CarInfo;

@Repository("auto51CarInfoDao")
public class Auto51CarInfoDaoImp implements Auto51CarInfoDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Auto51CarInfo get(String seqID) {
		return entityManager.find(Auto51CarInfo.class, seqID);
	}

	@Override
	public List<Auto51CarInfo> listByUrl(String url) {
		Query query = this.entityManager
				.createQuery("from Auto51CarInfo as c where c.carSourceUrl=:carSourceUrl ");
		query.setParameter("carSourceUrl", url);
		@SuppressWarnings("unchecked")
		List<Auto51CarInfo> auto51CarInfos = (List<Auto51CarInfo>) query
				.getResultList();
		return auto51CarInfos;
	}

	@Override
	public void persist(Auto51CarInfo auto51CarInfo) {
		this.entityManager.persist(auto51CarInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auto51CarInfo> listByStatusCodeAndLastActiveDateTime(
			int statusCode, Date lastActiveDateTime) {
//		Query query = this.entityManager
//				.createQuery("from Auto51CarInfo as c where c.statusType=:statusCode and lastActiveDate<=:lastActiveDateTime");
//		query.setParameter("statusCode", statusCode);
//		query.setParameter("lastActiveDateTime", lastActiveDateTime);
		
		Query query = this.entityManager
				.createQuery("from Auto51CarInfo as c where c.statusType=:statusCode and c.lastActiveDate<=:lastActiveDate");
		query.setParameter("statusCode", statusCode);
		query.setParameter("lastActiveDate", lastActiveDateTime);
		return (List<Auto51CarInfo>) query.getResultList();
	}

	@Override
	public List<Auto51CarInfo> listByUrlAndDeclareDate(String url, Date declareDate) {
		Query query = this.entityManager
				.createQuery("from Auto51CarInfo as c where c.carSourceUrl=:carSourceUrl and c.declareDate =:declareDate");
		query.setParameter("carSourceUrl", url);
		query.setParameter("declareDate", declareDate);
		
		@SuppressWarnings("unchecked")
		List<Auto51CarInfo> auto51CarInfos = (List<Auto51CarInfo>) query
				.getResultList();
		return auto51CarInfos;
	}
}
