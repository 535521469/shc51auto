package pp.corleone.auto51.service.detail;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51CarInfo.Auto51StatusCode;
import pp.corleone.auto51.domain.Auto51SellerInfo;

class Auto51DetailExtracterGroovyImp2 implements Auto51DetailExtracter {

	protected void fillPrice(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			String price = doc.select("span.car_color_in").first().select("em")
					.first().text();
			price = price + "万";
			auto51CarInfo.setPrice(price);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void fillLicenseDateAndRoadHaul(Document doc,
			Auto51CarInfo auto51CarInfo) {
		try {
			Elements infoElements = doc.select("p.car_info1");
			if (null != infoElements) {
				Element infoElement = infoElements.first();
				Elements strongElements = infoElement.select("strong");
				if (strongElements.size() == 2) {
					String licenseDate = strongElements.first().text();
					String roadHaul = strongElements.get(1).text();
					auto51CarInfo.setLicenseDate(licenseDate);
					auto51CarInfo.setRoadHaul(roadHaul);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void fillDisplacementAndGearbox(Document doc,
			Auto51CarInfo auto51CarInfo) {
		try {
			Elements infoElements = doc.select("p.car_info1");
			if (infoElements.size() > 1) {
				Element infoElement = infoElements.get(1);
				Elements strongElements = infoElement.select("strong");
				if (strongElements.size() == 2) {
					String displacement = strongElements.first().text();
					String gearbox = strongElements.get(1).text();
					auto51CarInfo.setDisplacement(displacement);
					auto51CarInfo.setGearbox(gearbox);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void fillColor(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			Elements infoElements = doc.select("p.car_info1");
			if (infoElements.size() > 2) {
				Element infoElement = infoElements.get(2);
				Elements strongElements = infoElement.select("strong");
				if (strongElements.size() > 1) {
					String color = strongElements.first().text();
					auto51CarInfo.setColor(color);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void fillManufacturerAndBrand(Document doc,
			Auto51CarInfo auto51CarInfo) {
		try {
			Element paramElement = doc.select("dd.car_dsc_1>dl.params").first();

			String manufacturer = paramElement.select("dd").first().select("b")
					.first().text();
			auto51CarInfo.setManufacturer(manufacturer);

			String brand = doc
					.select("div.daohang>a[href^=http://www.51auto.com/search/s-]")
					.first().text();
			brand = brand.replace("浜屾墜", "");
			auto51CarInfo.setBrand(brand);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void fillTitle(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			Element titleElement = doc.select("strong.car_title").first();
			String title = "";
			if (titleElement != null) {
				title = titleElement.text();
				auto51CarInfo.setTitle(title);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void fillContacterPhone(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			Element contacterPhoneElement = doc.select("div.car_ask>p").first();
			String contacterPhone = "";
			if (contacterPhoneElement != null) {
				contacterPhone = contacterPhoneElement.text();
				String[] phoneB_A = contacterPhone.split("电话：");
				if (phoneB_A.length == 2) {
					String phone_After = phoneB_A[1];
					int phoneEndIndex = phone_After.indexOf("【");
					contacterPhone = phone_After.substring(0, phoneEndIndex);
					contacterPhone = contacterPhone.replace(".", "").trim();
					auto51CarInfo.setContacterPhone(contacterPhone);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Auto51CarInfo buildAuto51CarInfo(Document doc) {
		Auto51CarInfo auto51CarInfo = new Auto51CarInfo();
		this.fillPrice(doc, auto51CarInfo);
		this.fillLicenseDateAndRoadHaul(doc, auto51CarInfo);
		this.fillColor(doc, auto51CarInfo);
		this.fillDisplacementAndGearbox(doc, auto51CarInfo);
		this.fillManufacturerAndBrand(doc, auto51CarInfo);
		this.fillTitle(doc, auto51CarInfo);
		this.fillContacterPhone(doc, auto51CarInfo);

		if (this.isOnline(doc)) {
			Date now = new Date();
			auto51CarInfo.setFetchDate(now);
			auto51CarInfo.setLastActiveDate(now);

			auto51CarInfo.setStatusType(Auto51StatusCode.STATUS_TYPE_FOR_SALE);
		} else {
			auto51CarInfo.setStatusType(Auto51StatusCode.STATUS_TYPE_SOLD);
		}

		return auto51CarInfo;
	}

	public boolean isOnline(Document doc) {
		return doc.select("div.offline").first() == null;
	}

	protected Auto51SellerInfo buildAuto51SellerInfo(Document doc) {
		Auto51SellerInfo auto51SellerInfo = new Auto51SellerInfo();
		Element pElement = doc.select("p.car_shop_info").first();
		if (pElement != null) {
			String cityNameLiteral = "公司名称：";
			String shopLevelLiteral = "商铺级别：";
			// String shopAddressLiteral = "商铺地址：";
			// Elements spanElements = pElement.select("span");
			String info = pElement.text();
			String shopName = info.substring(info.indexOf(cityNameLiteral)
					+ cityNameLiteral.length(), info.indexOf(shopLevelLiteral));
			auto51SellerInfo.setShopName(shopName.trim());
		}
		Element btn_shop = doc.select("a.btn_shop").first();
		if (btn_shop != null) {
			String shopUrl = btn_shop.attr("href");
			// auto51SellerInfo.setShopUrl(shopUrl);
			String zhuanqv = "http://www.51auto.com/hclist__HAPU__";
			if (shopUrl.startsWith(zhuanqv)) {
				String carID = shopUrl.substring(zhuanqv.length(),
						shopUrl.indexOf("_UI__"));
				auto51SellerInfo
						.setShopUrl("http://www.51auto.com/control/HappyDealersList?p=p&zoneID=-1&zoneID=-1&happyUserId="
								+ carID
								+ "&sort=-1&level=-1&page=1&keyWord="
								+ auto51SellerInfo.getShopName());
			} else {
				auto51SellerInfo.setShopUrl(shopUrl);
			}

		}
		return auto51SellerInfo;
	}

	@Override
	public Auto51CarInfo getCarInfo(Document doc) {
		Auto51CarInfo auto51CarInfo = this.buildAuto51CarInfo(doc);
		Auto51SellerInfo auto51SellerInfo = this.buildAuto51SellerInfo(doc);
		if (auto51SellerInfo.getShopUrl() != null) {
			auto51CarInfo.setAuto51SellerInfo(auto51SellerInfo);
			auto51CarInfo.setShopUrl(auto51SellerInfo.getShopUrl());
		}
		return auto51CarInfo;
	}
}
