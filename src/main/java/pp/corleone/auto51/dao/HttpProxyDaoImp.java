package pp.corleone.auto51.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
				.createQuery("from HttpProxy as hp where hp.validFlag =:validFlag and fetchDate = :fetchDate ");
		query.setParameter("validFlag", HttpProxy.ValidFlagEnum.Valid.getCode());
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		query.setParameter("fetchDate", c.getTime());
		@SuppressWarnings("unchecked")
		List<HttpProxy> httpProxies = query.getResultList();
		return httpProxies;
	}
}
