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

public class Auto51SellerExtracterGroovyImpTest4 {

	private static Document doc = null;
	private static Auto51SellerExtracter auto51SellerExtracter = null;
	private static Auto51CarInfo auto51CarInfo = null;

	@BeforeClass
	public static void beforeClass() {
		String shopUrl = "http://www.51auto.com/control/HappyDealersList?p=p&zoneID=-1&zoneID=-1&happyUserId=234927&sort=-1&level=-1&page=1&keyWord=浙江康桥锦澳奥迪";
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

		System.out.println(auto51SellerInfo.getShopAddress());
		System.out.println("浙江省杭州市临平藕花洲大街西段151号");
		assertEquals("浙江省杭州市临平藕花洲大街西段151号", auto51SellerInfo.getShopAddress());
		assertEquals("0571-89366060", auto51SellerInfo.getContactPhone());
		assertEquals("方志强", auto51SellerInfo.getContacter());
	}
}
