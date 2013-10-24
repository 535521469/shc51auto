package pp.corleone.auto51.service.detail;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pp.corleone.Log;
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51CarInfo.Auto51StatusCode;
import pp.corleone.auto51.domain.Auto51SellerInfo;

class Auto51DetailExtracterGroovyImp implements Auto51DetailExtracter {
	protected static void LogException(Document doc, String msg) {
		Log.error(msg + ".." + doc.baseUri());
	}

	protected void fillPrice(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			String price = doc.select("dl.or_dl").select("b.red").first()
					.text();
			price = price + "万";
			auto51CarInfo.setPrice(price);
		} catch (Exception e) {
			LogException(doc, "price");
		}
	}

	protected void fillLicenseDateAndRoadHaul(Document doc,
			Auto51CarInfo auto51CarInfo) {
		try {
			Elements infoElements = doc.select("dl.or_dl");

			if (null == infoElements) {
				LogException(doc, "license date or road haul");
			} else {
				String licenseDate = "";
				String roadHaul = "";
				Element infoElement = infoElements.first();
				for (Element pEle : infoElement.select("p.odd_left")) {
					if ("首次上牌：".equals(pEle.select("i").first().text())) {
						licenseDate = pEle.text().replace("首次上牌：", "");
					}
				}
				for (Element pEle : infoElement.select("p.odd_right")) {
					if ("行驶里程：".equals(pEle.select("i").first().text())) {
						roadHaul = pEle.text().replace("行驶里程：", "");
					}
				}

				if (StringUtils.isBlank(roadHaul)) {
					LogException(doc, "road haul");
				} else {
					auto51CarInfo.setRoadHaul(roadHaul);
				}

				if (StringUtils.isBlank(licenseDate)) {
					LogException(doc, "licenseDate");
				} else {
					auto51CarInfo.setLicenseDate(licenseDate);
				}

			}

		} catch (Exception e) {
			LogException(doc, "license date or road haul");
		}
	}

	protected void fillDisplacementAndGearbox(Document doc,
			Auto51CarInfo auto51CarInfo) {
		try {
			Elements infoElements = doc.select("dl.or_dl");
			if (null == infoElements) {
				LogException(doc, "displacement or gear box");
			} else {
				String displacement = "";
				String gearbox = "";
				Element infoElement = infoElements.first();
				for (Element pEle : infoElement.select("p.odd_left")) {
					if ("排      量：".equals(pEle.select("i").first().text())) {
						displacement = pEle.text().replace("排      量：", "");
						break;
					}
				}
				if (StringUtils.isBlank(displacement)) {
					LogException(doc, "displacement");
				} else {
					auto51CarInfo.setDisplacement(displacement);
				}

				for (Element pEle : infoElement.select("p.odd_right")) {
					if ("变 速 箱 ：".equals(pEle.select("i").first().text())) {
						gearbox = pEle.text().replace("变 速 箱 ：", "");
						break;
					}
				}
				if (StringUtils.isBlank(gearbox)) {
					LogException(doc, "gearbox");
				} else {
					auto51CarInfo.setGearbox(gearbox);
				}
			}

		} catch (Exception e) {
			LogException(doc, "displacement or gear box");
		}
	}

	protected void fillColor(Document doc, Auto51CarInfo auto51CarInfo) {

		try {
			Elements infoElements = doc.select("dl.or_dl");
			if (null == infoElements) {
				LogException(doc, "color");
			} else {
				String color = "";
				Element infoElement = infoElements.first();
				for (Element pEle : infoElement.select("p.odd_left")) {
					if ("车身颜色：".equals(pEle.select("i").first().text())) {
						color = pEle.text().replace("车身颜色：", "");
						break;
					}
				}
				if (StringUtils.isBlank(color)) {
					LogException(doc, "color");
				} else {
					auto51CarInfo.setColor(color);
				}

			}

		} catch (Exception e) {
			LogException(doc, "color");
		}
	}

	protected void fillManufacturer(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			Element paramElement = doc.select("div.ptb_right").first();

			Elements manufacturers = paramElement.select("td");

			String manufacturerString = "";
			for (Element manufacturer : manufacturers) {
				if ("品牌".equals(manufacturer.text())) {

					manufacturerString = manufacturer.nextElementSibling()
							.text();
					break;
				}
			}
			auto51CarInfo.setManufacturer(manufacturerString);
			if (StringUtils.isBlank(auto51CarInfo.getManufacturer())) {
				LogException(doc, "manufacturer");
			}

		} catch (Exception e) {
			LogException(doc, "manufacturer");
		}
	}

	protected void fillBrand(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			String title = doc.title();
			String brand = title.substring(title.indexOf("二手"),
					title.indexOf("】"));
			brand = brand.replace("二手", "");
			auto51CarInfo.setBrand(brand);
		} catch (Exception e) {
			LogException(doc, "brand");
		}
	}

	protected void fillTitle(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			Element titleElement = doc.select("div.ocar_imp>h1").first();
			String title = "";
			if (titleElement != null) {
				title = titleElement.text();
			}
			auto51CarInfo.setTitle(title);
			if (StringUtils.isBlank(auto51CarInfo.getTitle())) {
				LogException(doc, "title");
			}
		} catch (Exception e) {
			LogException(doc, "title");
		}
	}

	protected void fillContacterPhone(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			Element contacterPhoneElement = doc.select("div.div_font").first();
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
			LogException(doc, "Contacter Phone");
		}
	}

	protected void buildLookCarAddress(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			Element lookCarElement = doc.select("p.lookcar>i").first();
			Element labelElement = doc.select("p.lookcar>span.gray3").first();

			String value = lookCarElement.text();
			;
			if ("看车地点：".equals(labelElement.text())) {
				auto51CarInfo.setParkAddress(value.replace(" 【查看地图】", ""));
			}

		} catch (Exception e) {
			LogException(doc, "LookCarAddress or contacter");
		}
	}

	protected Auto51CarInfo buildAuto51CarInfo(Document doc) {
		Auto51CarInfo auto51CarInfo = new Auto51CarInfo();
		this.fillPrice(doc, auto51CarInfo);
		this.fillLicenseDateAndRoadHaul(doc, auto51CarInfo);
		this.fillColor(doc, auto51CarInfo);
		this.fillDisplacementAndGearbox(doc, auto51CarInfo);
		this.fillBrand(doc, auto51CarInfo);
		this.fillManufacturer(doc, auto51CarInfo);
		this.fillTitle(doc, auto51CarInfo);
		this.fillContacterPhone(doc, auto51CarInfo);

		this.buildLookCarAddress(doc, auto51CarInfo);

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
		Element pElement = doc.select("dl.sm_dl").first();
		String shopName = "";
		if (pElement == null) {
			// do nothing
		} else {
			Element shopElement = pElement.select("a.a_ktit").first();
			if (shopElement == null) {
				Element btn_shop = doc.select("a.btn_shop").first();
				if (btn_shop == null) {
					// jing xiao shang
					Element shopNameElement = pElement.select("dt").first()
							.select("span").first();
					shopName = shopNameElement.text();
					auto51SellerInfo.setShopName(shopName);
					auto51SellerInfo
							.setShopUrl("http://www.51auto.com/control/FindDealerList?dealerType=&pageNo=&brand=&Clear=Clear&provinceId=&zoneId=&dealerName="
									+ shopName);
				} else {
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
			} else {
				shopName = shopElement.text();
				auto51SellerInfo.setShopName(shopName);
				auto51SellerInfo.setShopUrl(shopElement.attr("href"));
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
