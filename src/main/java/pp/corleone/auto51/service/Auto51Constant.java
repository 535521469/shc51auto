package pp.corleone.auto51.service;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Auto51Constant {

	private Properties properties;

	private ApplicationContext applicationContext;

	{
		try {
			this.properties = PropertiesLoaderUtils
					.loadProperties(new ClassPathResource("auto51.properties"));

			this.applicationContext = new ClassPathXmlApplicationContext(
					"applicationContext.xml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object getBean(String beanID) {
		return this.applicationContext.getBean(beanID);
	}

	private static Auto51Constant auto51Constant;

	private Auto51Constant() {
	}

	public String getProperty(String key, String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}

	public Integer getProperty(String key, Integer defaultValue) {
		return this.properties.containsKey(key) ? Integer
				.valueOf(this.properties.getProperty(key)) : defaultValue;
	}

	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}

	public static Auto51Constant getInstance() {
		if (auto51Constant == null) {
			auto51Constant = new Auto51Constant();
		}
		return auto51Constant;
	}

	public final static String homePage = "http://www.51auto.com/";

	public final static String CITIES = "cities";

	public final static String ONGOING_FLAG = "ongoing_flag";

	public final static String FETCHER_SLEEP_MILLISECOND = "FETCHER_SLEEP_MILLISECOND";
	public final static String STATUS_CHECK_FLAG = "status_check_flag";
	public final static String ONGOING_CYCLE_DELAY = "ongoing_cycle_delay";

	public final static String IGNORE_INCOMPLETE_SELLER = "ignore_imcomplete_seller";

	public final static String FETCHER_IDLE_SLEEP = "fetcher_idle_sleep";
	public final static String STATUS_CARRIER_IDLE_SLEEP = "status_carrIer_idle_sleep";

	public final static String STATUS_DELAY = "status_delay";
	public final static String STATUS_CHECK_RANGE = "status_check_range";
	public final static String AHEAD_OF_TIME = "ahead_of_time";

	public final static String LIST = "LIST";
	public final static String DETAIL = "DETAIL";
	public final static String CAR_INFO = "CARINFO";
	public final static String SELLER_INFO = "SELLER_INFO";
	public final static String FIRST_PAGE = "FIRST_PAGE";

	// public final static String BUILD_ALL_PAGE = "BUILD_ALL_PAGE";

	private static String buildCityUrl(String url) {
		String city = url.replace(homePage, "");
//		return homePage + "search/" + city;
		
		 return homePage + city;
	}

	public static String buildShopUrl(String url) {
		return buildCityUrl(url) + "pabmdcig3f/?ordering=publishTime&page=1";
	}

	public static String buildPersonalUrl(String url) {
		return buildCityUrl(url) + "pabmdcig2f/?ordering=publishTime&page=1";
	}

}
