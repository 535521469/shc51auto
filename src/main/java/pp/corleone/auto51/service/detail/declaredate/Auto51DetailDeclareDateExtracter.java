package pp.corleone.auto51.service.detail.declaredate;

import org.jsoup.nodes.Document;

import pp.corleone.auto51.domain.Auto51CarInfo;

public interface Auto51DetailDeclareDateExtracter {

	public abstract void fillDeclareDate(Document doc,
			Auto51CarInfo auto51CarInfo);

}
