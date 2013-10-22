package pp.corleone.auto51.service.detail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import pp.corleone.Log;
import pp.corleone.auto51.domain.Auto51CarInfo;

public class Auto51DetailExtracterGroovyImpTest {

	private static Document doc = null;
	private static Auto51DetailExtracter auto51DetailExtracter = null;
	private static Auto51CarInfo auto51CarInfo = null;

	@BeforeClass
	public static void beforeClass() {
		try {
			doc = Jsoup.connect("http://www.51auto.com/buycar/1881679.html")
					.get();
		} catch (IOException e) {
			fail("set up , connection fail");
			Log.error("", e);
		}
		auto51DetailExtracter = new Auto51DetailExtracterGroovyImp();
		auto51CarInfo = auto51DetailExtracter.getCarInfo(doc);
	}

	@Test
	public void testbuildLookCarAddress() {
		assertEquals("CBD店/望京店/鸟巢店/石景山店", auto51CarInfo.getAuto51SellerInfo().getShopName());
	}

	@Test
	public void testbuildAuto51SellerInfo() {
		assertEquals("北京卓杰行二手车", auto51CarInfo.getAuto51SellerInfo().getShopName());
		String shopUrl = "http://www.51auto.com/dealers/75339.html";
		
		System.out.println(shopUrl);
		System.out.println(auto51CarInfo.getAuto51SellerInfo().getShopUrl());
		assertEquals(shopUrl, auto51CarInfo.getAuto51SellerInfo().getShopUrl());
	}

	@Test
	public void testfillPrice() {
		assertEquals("3.50万", auto51CarInfo.getPrice());
	}

	@Test
	public void testfillLicenseDateAndRoadHaul() {
		assertEquals("2010年6月(3年)", auto51CarInfo.getLicenseDate());
		assertEquals("4.30万公里", auto51CarInfo.getRoadHaul());
	}

	@Test
	public void testfillDisplacementAndGearbox() {
		assertEquals("1.4L", auto51CarInfo.getDisplacement());
		assertEquals("手动", auto51CarInfo.getGearbox());
	}

	@Test
	public void testfillColor() {
		assertEquals("灰色/深内饰", auto51CarInfo.getColor());
	}

	@Test
	public void testfillManufacturerAndBrand() {
		assertEquals("北斗星", auto51CarInfo.getBrand());
		assertEquals("昌河铃木", auto51CarInfo.getManufacturer());
	}

	@Test
	public void testfillTitle() {
		assertEquals("【北京】 昌河铃木 北斗星 ES 手动 1.4L (国IV+OBD)", auto51CarInfo.getTitle());
	}

	@Test
	public void testfillContacterPhone() {
		assertEquals("18001325177 4006610608", auto51CarInfo.getContacterPhone());
	}
}
