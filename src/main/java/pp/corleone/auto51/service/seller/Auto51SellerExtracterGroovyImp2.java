package pp.corleone.auto51.service.seller;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import pp.corleone.auto51.domain.Auto51SellerInfo;

//@Component("auto51SellerExtracter")
public class Auto51SellerExtracterGroovyImp2 implements Auto51SellerExtracter {

	@Override
	public void fillSellerInfo(Document doc, Auto51SellerInfo auto51SellerInfo) {
		if (auto51SellerInfo.getShopUrl().startsWith(
				"http://www.51auto.com/dealers/")) {
			this.fillNormalSellerInfo(doc, auto51SellerInfo);
		} else {
			this.fillSpecialSellerInfo(doc, auto51SellerInfo);
			this.fillSpecialSellerInfo2(doc, auto51SellerInfo);
		}
	}

	protected void fillSpecialSellerInfo(Document doc,
			Auto51SellerInfo auto51SellerInfo) {

		if (auto51SellerInfo.getShopAddress() == null) {
			Element divElement = doc.select("div.ppjxs").first();
			if (divElement != null) {
				Elements tdElements = divElement.select("table>tbody>tr")
						.get(1).select("td");
				if (tdElements.size() == 7) {
					auto51SellerInfo.setContactPhone(tdElements.get(3).text());
					auto51SellerInfo.setShopAddress(tdElements.get(4).text());
					auto51SellerInfo.setContacter(tdElements.get(6).text());
				}
			}
		}
	}

	// <ul class="ul_row">
	// <li>
	// <strong class="nameblod"><a
	// href="http://www.51auto.com/hcarlist_-1_-1_-1_-1_555978_-1_-1_589874_-1_-1_10_1/"
	// target="_blank">广汽丰田广州长和永平店</a></strong>
	// <dl>
	// <dt>
	// <span class="auto"><img width="120" height="90"></span>
	// </dt>
	// <dd>
	// 地址：广东省广州市白云区白云大道北1365号（白云堡立交桥直行100米左侧）<br>
	// 邮编： <br>
	// 联系人：欧阳炳恒<br>
	// 联系电话：13688898336<br>
	// 电子邮件：44A90_longyonglai@dlr.gtmc.com.cn</dd>
	// </dl>
	// </li>
	// </ul>
	protected void fillSpecialSellerInfo2(Document doc,
			Auto51SellerInfo auto51SellerInfo) {

		if (auto51SellerInfo.getShopAddress() == null) {
			Element divElement = doc.select("ul.ul_row").first();
			if (divElement != null) {
				Element ddElement = divElement.select("li>dl>dd").first();
				if (ddElement != null) {
					String html = ddElement.html();

					String[] brStrings = html.split("<br /> ");

					for (int i = 0; i < brStrings.length; i++) {
						String info = brStrings[i];
						String addressString = "地址：";
						String contacterString = "联系人：";
						String phoneString = "联系电话：";
						if (info.startsWith(addressString)) {
							auto51SellerInfo.setShopAddress(info.replace(
									addressString, "").trim());
						} else if (info.startsWith(contacterString)) {
							auto51SellerInfo.setContacter(info.replace(
									contacterString, "").trim());
						} else if (info.startsWith(phoneString)) {
							auto51SellerInfo.setContactPhone(info.replace(
									phoneString, "").trim());
						}
					}
				}
			}
		}
	}

	protected void fillNormalSellerInfo(Document doc,
			Auto51SellerInfo auto51SellerInfo) {

		String address = doc.select("span[class=add tc]").text();
		address = address.replace("[查看地图]", "");

		auto51SellerInfo.setShopAddress(address);

		if (auto51SellerInfo.getShopName() == null) {

			auto51SellerInfo.setShopName(doc.select("i[class=fl]").first()
					.text());
		}

		Elements pElements = doc.select("div[class^=info_cont]");
		for (Element pElement : pElements.select("p")) {
			String pTextString = pElement.text();
			String wireTelephoneString = "固定电话：";
			String mobilePhoneString = "手机：";
			String _400 = "400电话：";
			if (pTextString.startsWith(wireTelephoneString)) {
				auto51SellerInfo.setWireTelephone(pTextString.replace(
						wireTelephoneString, ""));
			} else if (pTextString.startsWith(mobilePhoneString)) {
				auto51SellerInfo.setMobilePhone(pTextString.replace(
						mobilePhoneString, ""));
			} else if (pTextString.startsWith(_400)) {
				auto51SellerInfo
						.setWireTelephone(pTextString.replace(_400, ""));
			}
		}
	}
}
