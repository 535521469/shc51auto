package pp.corleone.auto51.service.list;

import java.util.List;

import org.jsoup.nodes.Document;

public interface Auto51ListExtracter {

	// public abstract Auto51CarInfo buildCityUrlMap(Document doc,
	// Auto51CarInfo auto51CarInfo);

	public List<String> getDetailUrls(Document doc);
	public List<String> getNextPageUrls(Document doc);

	
}
