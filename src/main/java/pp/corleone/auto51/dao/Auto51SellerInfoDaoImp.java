package pp.corleone.auto51.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import pp.corleone.auto51.domain.Auto51SellerInfo;

@Repository("auto51SellerInfoDao")
public class Auto51SellerInfoDaoImp implements Auto51SellerInfoDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Auto51SellerInfo get(String seqID) {
		return entityManager.find(Auto51SellerInfo.class, seqID);
	}

	@Override
	public Auto51SellerInfo listByName(String sellerName) {

		/**
		 * Query query = this.entityManager .createQuery(
		 * "from Auto51CarInfo as c where c.carSourceUrl=:carSourceUrl ");
		 * query.setParameter("carSourceUrl", url);
		 * 
		 * @SuppressWarnings("unchecked") List<Auto51CarInfo> auto51CarInfos =
		 *                                (List<Auto51CarInfo>) query
		 *                                .getResultList(); return
		 *                                auto51CarInfos;
		 */
		Query query = this.entityManager
				.createQuery("from Auto51SellerInfo as s where s.shopName=:shopName ");
		query.setParameter("shopName", sellerName);

		@SuppressWarnings("unchecked")
		List<Auto51SellerInfo> auto51SellerInfos = (List<Auto51SellerInfo>) query
				.getResultList();

		if (auto51SellerInfos.size() == 1) {
			return auto51SellerInfos.get(0);
		}

		return null;
	}
}
