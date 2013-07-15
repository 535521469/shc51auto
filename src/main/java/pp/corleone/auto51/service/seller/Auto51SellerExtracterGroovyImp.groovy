package pp.corleone.auto51.service.seller;

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Component

import pp.corleone.auto51.domain.Auto51SellerInfo
@Component("auto51SellerExtracter")
public class Auto51SellerExtracterGroovyImp implements Auto51SellerExtracter {

	@Override
	public void fillSellerInfo(Document doc, Auto51SellerInfo auto51SellerInfo) {
		if (auto51SellerInfo.getShopUrl().startsWith("http://www.51auto.com/dealers/")) {
			this.fillNormalSellerInfo(doc, auto51SellerInfo);
		}else{
			this.fillSpecialSellerInfo(doc, auto51SellerInfo);
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

	protected void fillNormalSellerInfo(Document doc, Auto51SellerInfo auto51SellerInfo){

		String address =doc.select("span[class=add tc]").text();
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
				auto51SellerInfo.setWireTelephone(pTextString.replace(
						_400, ""));
			}
		}
	}
}
