package pp.corleone.auto51.dao;

import pp.corleone.auto51.domain.Auto51SellerInfo;

public interface Auto51SellerInfoDao {

	public abstract Auto51SellerInfo get(String sqlID);

	public abstract Auto51SellerInfo listByName(String sellerName);

}
