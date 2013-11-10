package pp.corleone.auto51.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;

import pp.corleone.domain.HttpProxy;

@Repository("httpProxyDao")
public class HttpProxyDaoImp implements HttpProxyDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Collection<HttpProxy> getHttpProxies() {

		Query query = this.entityManager
				.createQuery("from HttpProxy as hp where hp.validFlag =:validFlag and fetchDate between :start and :end");
		query.setParameter("validFlag", HttpProxy.ValidFlagEnum.Valid.getCode());
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		query.setParameter("start", start.getTime(), TemporalType.TIMESTAMP);
		Calendar end = Calendar.getInstance();
		end.set(Calendar.HOUR, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		query.setParameter("end", end.getTime(), TemporalType.TIMESTAMP);
		@SuppressWarnings("unchecked")
		List<HttpProxy> httpProxies = query.getResultList();
		return httpProxies;
	}
}
