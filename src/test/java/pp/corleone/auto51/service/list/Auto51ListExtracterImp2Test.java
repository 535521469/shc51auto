package pp.corleone.auto51.service.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import pp.corleone.Log;

public class Auto51ListExtracterImp2Test {
	private static Auto51ListExtracter extracter;
	private static Document doc;

	@BeforeClass
	public static void beforeClass() {
		String url = "http://www.51auto.com/search/beijing/pabmdcig2f/?ordering=publishTime&page=1";
		try {
			doc = Jsoup.connect(url).get();
			extracter = new Auto51ListExtracterImp2();
		} catch (IOException e) {
			Log.error("",e);
			fail("set up , connection fail");
		}
	}

	@Test
	public void getDetailUrls() {
		List<String> detailUrls = extracter.getDetailUrls(doc);
		assertEquals(detailUrls.size(), 25);
	}

	@Test
	public void getNextUrl() {
		List<String> nextUrls = extracter.getNextPageUrls(doc);
		assertEquals(nextUrls.size(), 1);
	}
}
