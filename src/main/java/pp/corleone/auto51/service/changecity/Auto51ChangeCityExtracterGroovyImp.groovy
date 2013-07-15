package pp.corleone.auto51.service.changecity

import java.util.HashMap;
import java.util.Map
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("auto51ChangeCityExtracter")
class Auto51ChangeCityExtracterGroovyImp implements Auto51ChangeCityExtracter {

	
	@Override
	public Map<String, String> buildCityUrlMap(Document doc, Set<String> citys) {
		Map<String, Element> cityMap = this.buildCityMap(doc);
		Map<String, String> cityUrlMap = this.buildCityUrlMap(cityMap, citys);
		return cityUrlMap;
	}

	protected Map<String, Element> buildCityMap(Document doc) {
		Map<String, Element> cityMap = new HashMap<String, Element>();
		for (Element a : doc.select("a[href]")) {
			String cityName = a.text();
			if (!cityMap.containsKey(cityName)) {
				cityMap.put(a.text().trim(), a);
			}
		}
		return cityMap;
	}

	protected Map<String, String> buildCityUrlMap(Map<String, Element> cityMap,
			Set<String> citys) {
		Map<String, String> cityUrlMap = new HashMap<String, String>();
		for (String cityName : citys) {
			Element cityElement = cityMap.get(cityName);
			if (cityElement == null) {
				LoggerFactory.getLogger(this.getClass()).error(
						"city not exists" + cityName);
			} else {
				String url = cityElement.attr("href");
				LoggerFactory.getLogger(this.getClass()).info(
						"get " + cityName + " " + url);
				cityUrlMap.put(cityName, url);
				continue;
			}
		}
		return cityUrlMap;
	}

}
