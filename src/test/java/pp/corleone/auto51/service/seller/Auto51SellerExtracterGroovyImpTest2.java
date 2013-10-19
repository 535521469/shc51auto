package pp.corleone.auto51.service.seller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import pp.corleone.Log;
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51SellerInfo;

public class Auto51SellerExtracterGroovyImpTest2 {

	private static Document doc = null;
	private static Auto51SellerExtracter auto51SellerExtracter = null;
	private static Auto51CarInfo auto51CarInfo = null;

	@BeforeClass
	public static void beforeClass() {
		String shopUrl = "http://www.51auto.com/control/HappyDealersList?p=p&zoneID=-1&zoneID=-1&happyUserId=157184&sort=-1&level=-1&page=1&keyWord=广州风亚";
		try {
			doc = Jsoup.connect(shopUrl).get();
			auto51CarInfo = new Auto51CarInfo();
			Auto51SellerInfo auto51SellerInfo = new Auto51SellerInfo();
			auto51CarInfo.setAuto51SellerInfo(auto51SellerInfo);
			auto51SellerInfo.setShopUrl(shopUrl);
			auto51SellerExtracter = new Auto51SellerExtracterGroovyImp2();
			auto51SellerExtracter.fillSellerInfo(doc, auto51SellerInfo);
		} catch (IOException e) {
			Log.error("",e);
			fail("set up , connection fail");
		}
	}

	@Test
	public void fillSellerInfo() {
		Auto51SellerInfo auto51SellerInfo = auto51CarInfo.getAuto51SellerInfo();
		assertEquals("广州市白云区黄石东路701号", auto51SellerInfo.getShopAddress());
		assertEquals("020-36403863", auto51SellerInfo.getContactPhone());
		assertEquals("江贺联", auto51SellerInfo.getContacter());
	}
}
