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

public class Auto51SellerExtracterGroovyImpTest {

	private static Document doc = null;
	private static Auto51SellerExtracter auto51SellerExtracter = null;
	private static Auto51CarInfo auto51CarInfo = null;

	@BeforeClass
	public static void beforeClass() {
		String carUrl = "http://www.51auto.com/buycar/1683912.html";
		String shopUrl = "http://www.51auto.com/dealers/443358.html";
		try {
			doc = Jsoup.connect(shopUrl).get();
			auto51CarInfo = new Auto51CarInfo();
			auto51CarInfo.setCarSourceUrl(carUrl);
			Auto51SellerInfo auto51SellerInfo = new Auto51SellerInfo();
			auto51CarInfo.setAuto51SellerInfo(auto51SellerInfo);
			auto51SellerInfo.setShopUrl(shopUrl);
			auto51SellerExtracter = new Auto51SellerExtracterGroovyImp();
			auto51SellerExtracter.fillSellerInfo(doc, auto51SellerInfo);
		} catch (IOException e) {
			e.printStackTrace();
			fail("set up , connection fail");
		}
	}

	@Test
	public void fillSellerInfo() {
		Auto51SellerInfo auto51SellerInfo = auto51CarInfo.getAuto51SellerInfo();
		assertEquals("浙江省杭州市石祥路589号国际会展中心C馆8号曙光办公室",
				auto51SellerInfo.getShopAddress());
		assertEquals("18263588888 13738075999",
				auto51SellerInfo.getMobilePhone());
		assertEquals("0571-28977123", auto51SellerInfo.getWireTelephone());
		assertEquals("杭州曙光精品车行", auto51SellerInfo.getShopName());
	}
}
