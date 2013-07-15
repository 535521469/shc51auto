package pp.corleone.auto51.service.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("auto51ListExtracter")
public class Auto51ListExtracterGroovyImp implements Auto51ListExtracter {

	@Override
	public List<String> getDetailUrls(Document doc) {
		List<String> detailUrls = new ArrayList<String>();
		Elements carListElements = doc.select("ul#carList>li");
		for (Element carElement : carListElements) {
			Element divElement = carElement.select("div.s_c02").first();
			if (divElement != null) {
				Element strongEle = divElement.select("strong").first();
				if (strongEle != null) {
					Element aEle = strongEle.select("a").first();
					if (aEle != null) {
						String detailUrl = aEle.attr("href");
						if (!StringUtils.isBlank(detailUrl)) {
							String url = detailUrl.subSequence(detailUrl.indexOf("http://www.51auto.com/buycar/"), detailUrl.length()).replace("h_", "");
							detailUrls.add(url);
							LoggerFactory.getLogger(this.getClass()).debug(detailUrl+"-->"+url);
						}
					}
				}
			}
		}
		return detailUrls;
	}

	@Override
	public List<String> getNextPageUrls(Document doc) {
		List<String> nextPageUrls = new ArrayList<String> ();
		Element divElement = doc.select("div.fenye").first();
		if (divElement!=null) {
			int pages = Integer.valueOf(divElement.select("i.red").text());
			Element pageElement = divElement.select("a").first();
			String pageUrl = pageElement.attr("href");
			for (int i = 1; i <= pages; i++) {
				int eqIdx = pageUrl.lastIndexOf("=");
				String newUrl = pageUrl.substring(0, eqIdx)+"="+i;
				nextPageUrls.add(newUrl);
			}
		}
		return nextPageUrls;
	}
}
