package pp.corleone.auto51.service.detail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import pp.corleone.auto51.domain.Auto51CarInfo;

public class Auto51DetailDeclareDateUrlBuilderGroovyImpTest {

	private static Auto51CarInfo auto51CarInfo;
	private static Auto51DetailDeclareDateUrlBuilder auto51DetailDeclareDateUrlBuilder;
	private static Document doc;

	@BeforeClass
	public static void beforeClass() {
		String url = "http://www.51auto.com/buycar/1683912.html";
		try {
			doc = Jsoup.connect(url).get();
			auto51CarInfo = new Auto51CarInfo();
			auto51CarInfo.setCarSourceUrl(url);
			auto51DetailDeclareDateUrlBuilder = new Auto51DetailDeclareDateUrlBuilderGroovyImp();
		} catch (IOException e) {
			e.printStackTrace();
			fail("set up , connection fail");
		}
	}

	@Test
	public void getCarDeclareDateUrl() {
		String declareDateUrl = auto51DetailDeclareDateUrlBuilder
				.getCarDeclareDateUrl(auto51CarInfo, doc);

		String target = "http://www.51auto.com/dwr/exec/CarViewAJAX.getCarInfoNew?callCount=1&c0-scriptName=CarViewAJAX&c0-methodName=getCarInfoNew&c0-id=718_1373617483103&c0-param0=number:1683912&xml=true";

		System.out.println(target);
		System.out.println(declareDateUrl);

		assertEquals(declareDateUrl, target);

	}

}
