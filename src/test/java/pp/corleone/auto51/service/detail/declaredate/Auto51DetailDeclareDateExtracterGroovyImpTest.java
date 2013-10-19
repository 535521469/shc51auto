package pp.corleone.auto51.service.detail.declaredate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import pp.corleone.Log;
import pp.corleone.auto51.domain.Auto51CarInfo;

public class Auto51DetailDeclareDateExtracterGroovyImpTest {

	private static Auto51CarInfo auto51CarInfo;
	private static Auto51DetailDeclareDateExtracter auto51DetailDeclareDateExtracter;
	private static Document doc;

	@BeforeClass
	public static void beforeClass() {
		String url = "http://www.51auto.com/dwr/exec/CarViewAJAX.getCarInfoNew?callCount=1&c0-scriptName=CarViewAJAX&c0-methodName=getCarInfoNew&c0-id=718_1373617483103&c0-param0=number:1772764&xml=true";
		try {
			doc = Jsoup.connect(url).get();
			auto51CarInfo = new Auto51CarInfo();
			auto51CarInfo.setCarSourceUrl(url);
			auto51DetailDeclareDateExtracter = new Auto51DetailDeclareDateExtracterGroovyImp2();
		} catch (IOException e) {
			Log.error("",e);
			fail("set up , connection fail");
		}
	}

	@Test
	public void fillDeclareDate() {

		auto51DetailDeclareDateExtracter.fillDeclareDate(doc, auto51CarInfo);
		System.out.println(new DateTime(2013, 7, 1, 0, 0, 0, 0).toDate());
		System.out.println(auto51CarInfo.getDeclareDate());

		assertEquals(auto51CarInfo.getDeclareDate(), new DateTime(2013, 7, 1,
				0, 0, 0, 0).toDate());

	}
}
