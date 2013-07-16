package pp.corleone.auto51.dao;

import java.util.List;

import pp.corleone.auto51.domain.Auto51SellerInfo;

public interface Auto51SellerInfoDao {

	public abstract Auto51SellerInfo get(String sqlID);

	public abstract Auto51SellerInfo listByName(String sellerName);

	public abstract List<Auto51SellerInfo> listIncompletedSellers();

}
