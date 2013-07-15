package pp.corleone.auto51.service.changecity;

import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;

public interface Auto51ChangeCityExtracter {

	public abstract Map<String, String> buildCityUrlMap(Document doc,
			Set<String> citys);

}
