package pp.corleone.auto51.service.detail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import pp.corleone.auto51.domain.Auto51CarInfo;

public class Auto51DetailExtracterGroovyImpTest {

	private static Document doc = null;
	private static Auto51DetailExtracter auto51DetailExtracter = null;
	private static Auto51CarInfo auto51CarInfo = null;


	@BeforeClass
	public static void beforeClass() {
		try {
			doc = Jsoup.connect("http://www.51auto.com/buycar/1683912.html")
					.get();
		} catch (IOException e) {
			fail("set up , connection fail");
			e.printStackTrace();
		}
		auto51DetailExtracter = new Auto51DetailExtracterGroovyImp();
		auto51CarInfo = auto51DetailExtracter.getCarInfo(doc);
	}

	@Test
	public void testbuildAuto51SellerInfo() {
		assertEquals("北京千里行", auto51CarInfo.getAuto51SellerInfo().getShopName());
		String shopUrl = "http://www.51auto.com/control/HappyDealersList?p=p&zoneID=-1&zoneID=-1&happyUserId=157184&sort=-1&level=-1&page=1&keyWord=北京千里行";

		System.out.println(shopUrl);
		System.out.println(auto51CarInfo.getAuto51SellerInfo().getShopUrl());
		assertEquals(shopUrl, auto51CarInfo.getAuto51SellerInfo().getShopUrl());
	}

	@Test
	public void testfillPrice() {
		assertEquals("4.00万", auto51CarInfo.getPrice());
	}

	@Test
	public void testfillLicenseDateAndRoadHaul() {
		assertEquals("2009年11月1日", auto51CarInfo.getLicenseDate());
		assertEquals("6.27万公里", auto51CarInfo.getRoadHaul());
	}

	@Test
	public void testfillDisplacementAndGearbox() {
		assertEquals("1.5L", auto51CarInfo.getDisplacement());
		assertEquals("手动", auto51CarInfo.getGearbox());
	}

	@Test
	public void testfillColor() {
		assertEquals("红色", auto51CarInfo.getColor());
	}

	@Test
	public void testfillManufacturerAndBrand() {
		assertEquals("长安", auto51CarInfo.getManufacturer());
		assertEquals("悦翔", auto51CarInfo.getBrand());
	}

	@Test
	public void testfillTitle() {
		assertEquals("【北京】 长安 悦翔 三厢 尊贵型 手动 1.5L", auto51CarInfo.getTitle());
	}

	@Test
	public void testfillContacterPhone() {
		assertEquals("010-80794585", auto51CarInfo.getContacterPhone());
	}
}
