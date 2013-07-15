package pp.corleone.auto51.service.seller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51SellerInfo;

public class Auto51SellerExtracterGroovyImpTest3 {

	private static Document doc = null;
	private static Auto51SellerExtracter auto51SellerExtracter = null;
	private static Auto51CarInfo auto51CarInfo = null;

	@BeforeClass
	public static void beforeClass() {
		String shopUrl = "http://www.51auto.com/control/HappyDealersList?p=p&zoneID=-1&zoneID=-1&happyUserId=555978&sort=-1&level=-1&page=1&keyWord=广州长和";
		try {
			doc = Jsoup.connect(shopUrl).get();
			auto51CarInfo = new Auto51CarInfo();
			Auto51SellerInfo auto51SellerInfo = new Auto51SellerInfo();
			auto51CarInfo.setAuto51SellerInfo(auto51SellerInfo);
			auto51SellerInfo.setShopUrl(shopUrl);
			auto51SellerExtracter = new Auto51SellerExtracterGroovyImp2();
			auto51SellerExtracter.fillSellerInfo(doc, auto51SellerInfo);
		} catch (IOException e) {
			e.printStackTrace();
			fail("set up , connection fail");
		}
	}

	@Test
	public void fillSellerInfo() {
		Auto51SellerInfo auto51SellerInfo = auto51CarInfo.getAuto51SellerInfo();

		// System.out.println(auto51SellerInfo.getShopAddress());
		// System.out.println("广东省广州市白云区白云大道北1365号（白云堡立交桥直行100米左侧）");

		assertEquals("广东省广州市白云区白云大道北1365号（白云堡立交桥直行100米左侧）",
				auto51SellerInfo.getShopAddress());
		assertEquals("13688898336", auto51SellerInfo.getContactPhone());
		assertEquals("欧阳炳恒", auto51SellerInfo.getContacter());
	}
}
