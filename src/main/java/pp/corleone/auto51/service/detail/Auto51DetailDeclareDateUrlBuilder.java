package pp.corleone.auto51.service.detail;

import org.jsoup.nodes.Document;

import pp.corleone.auto51.domain.Auto51CarInfo;

public interface Auto51DetailDeclareDateUrlBuilder {

	public abstract String getCarDeclareDateUrl(Auto51CarInfo auto51CarInfo, Document doc);

}
