package pp.corleone.auto51.service.seller;

import org.jsoup.nodes.Document;

import pp.corleone.auto51.domain.Auto51SellerInfo;

public interface Auto51SellerExtracter {

	public abstract void fillSellerInfo(Document doc,
			Auto51SellerInfo auto51SellerInfo);

}
