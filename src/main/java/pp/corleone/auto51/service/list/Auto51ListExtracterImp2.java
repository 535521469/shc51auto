/**
 * modify 2013/09/15
 */
package pp.corleone.auto51.service.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import pp.corleone.Log;

@Component("auto51ListExtracter")
public class Auto51ListExtracterImp2 implements Auto51ListExtracter {

	@Override
	public List<String> getDetailUrls(Document doc) {
		List<String> detailUrls = new ArrayList<String>();
		Elements carListElements = doc.select("ul#carList>li");
		for (Element carElement : carListElements) {
			Element aEle = carElement.select("a").first();
			if (null == aEle) {
				// do nothing
			} else {
				String detailUrl = aEle.attr("href");
				if (!StringUtils.isBlank(detailUrl)) {
					String url = detailUrl.substring(
							detailUrl.indexOf("http://www.51auto.com/buycar/"),
							detailUrl.length()).replace("h_", "");
					detailUrls.add(url);
					Log.debug(detailUrl + "-->" + url);
				}
			}
		}
		return detailUrls;
	}

	@Override
	public List<String> getNextPageUrls(Document doc) {
		List<String> nextPageUrls = new ArrayList<String>();
		Element divElement = doc.select("div.fenye_new").first();
		int lastPageNo = 0;
		if (divElement != null) {
			Elements pageElements = divElement.select("a");

			for (Element pageElement : pageElements) {
				if ("末页".equals(pageElement.text().trim())) {
					String lastUrl = pageElement.attr("href");

					int pageExpIndex = lastUrl.indexOf("page=");
					String pageExpression = lastUrl.substring(pageExpIndex);
					int lastPageIndex = Integer.valueOf(
							pageExpression.replace("page=", "")).intValue();

					for (int i = 2; i < lastPageIndex + 1; i++) {
						String newUrl = lastUrl.substring(0, pageExpIndex)
								+ "page=" + i;
						nextPageUrls.add(newUrl);
						lastPageNo = i;
					}
				}
			}

		}

		if (nextPageUrls.size() > 0) {
			Log.info("get " + (lastPageNo - 1) + " pages from " + doc.baseUri()
					+ " last :" + nextPageUrls.get(nextPageUrls.size() - 1));
		}

		return nextPageUrls;
	}
}
