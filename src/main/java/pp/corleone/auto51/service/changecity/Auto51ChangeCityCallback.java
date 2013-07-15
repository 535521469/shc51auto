package pp.corleone.auto51.service.changecity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51CarInfo.Auto51SellerType;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.service.Callback;
import pp.corleone.service.DefaultCallback;
import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.DefaultRequestWrapper;
import pp.corleone.service.DefaultRequestWrapper.PriorityEnum;
import pp.corleone.service.DefaultResponseWrapper;
import pp.corleone.service.Fetcher;
import pp.corleone.service.FetcherConstants;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51ChangeCityCallback")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51ChangeCityCallback extends DefaultCallback {

	@Autowired
	private Auto51ChangeCityExtracter auto51ChangeCityExtracter;

	public Auto51ChangeCityExtracter getAuto51ChangeCityExtracter() {
		return auto51ChangeCityExtracter;
	}

	@Autowired
	public void setAuto51ChangeCityExtracter(
			Auto51ChangeCityExtracter auto51ChangeCityExtracter) {
		this.auto51ChangeCityExtracter = auto51ChangeCityExtracter;
	}

	private Set<String> cities;

	public Set<String> getCities() {
		return cities;
	}

	public void setCities(Set<String> cities) {
		this.cities = cities;
	}

	public Auto51ChangeCityCallback() {
	}

	public Auto51ChangeCityCallback(Set<String> cities) {
		this.setCities(cities);
	}

	protected Fetcher buildFetcher(String locate, String url,
			Auto51SellerType sellerType, Callback listCallback) {
		Auto51CarInfo ici = new Auto51CarInfo();
		ici.setSellerType(sellerType.getCode());
		ici.setCityName(locate);

		this.getLogger().debug(
				" sellertype " + ici.getSellerType() + ";locate:"
						+ ici.getCityName());

		if (Auto51SellerType.INDIVIDUAL == sellerType) {
			// individual
			DefaultRequestWrapper per = new DefaultRequestWrapper(
					Auto51Constant.buildPersonalUrl(url), listCallback, this
							.getResponseWrapper().getReferRequestWrapper(),
					PriorityEnum.LIST);
			per.getContext().put(Auto51Constant.CAR_INFO, ici);
			per.getContext().put(Auto51Constant.BUILD_ALL_PAGE, "1");
			DefaultFetcher fetcher = (DefaultFetcher) Auto51Constant
					.getInstance().getBean("auto51ListFetcher");

			per.setTimeout(20000);

			fetcher.setRequestWrapper(per);

			return fetcher;
		} else if (Auto51SellerType.SHOP == sellerType) {
			// shop
			DefaultRequestWrapper shop = new DefaultRequestWrapper(
					Auto51Constant.buildShopUrl(url), listCallback, this
							.getResponseWrapper().getReferRequestWrapper(),
					PriorityEnum.LIST);
			shop.getContext().put(Auto51Constant.CAR_INFO, ici);
			shop.getContext().put(Auto51Constant.BUILD_ALL_PAGE, 1);

			DefaultFetcher fetcher = (DefaultFetcher) Auto51Constant
					.getInstance().getBean("auto51ListFetcher");
			shop.setTimeout(20000);

			fetcher.setRequestWrapper(shop);
			return fetcher;
		}
		return null;
	}

	protected List<Fetcher> buildAllFetchers(Map<String, String> cityUrlMap) {
		List<Fetcher> fetchers = new ArrayList<Fetcher>();
		for (Entry<String, String> entry : cityUrlMap.entrySet()) {

			fetchers.add(this.buildFetcher(entry.getKey(), entry.getValue(),
					Auto51SellerType.INDIVIDUAL, (Callback) Auto51Constant
							.getInstance().getBean("auto51ListCallback")));

			fetchers.add(this.buildFetcher(entry.getKey(), entry.getValue(),
					Auto51SellerType.SHOP, (Callback) Auto51Constant
							.getInstance().getBean("auto51ListCallback")));
		}
		return fetchers;
	}

	@Override
	public Map<String, Collection<?>> call() throws Exception {
		Collection<Fetcher> fetchers = new ArrayList<Fetcher>();

		Document doc = ((DefaultResponseWrapper) this.getResponseWrapper())
				.getDoc();

		// Map<String, Element> cityMap = this.buildCityMap(doc);
		// Map<String, String> cityUrlMap = this.buildCityUrlMap(cityMap);
		Map<String, String> cityUrlMap = auto51ChangeCityExtracter
				.buildCityUrlMap(doc, this.getCities());

		this.getLogger().info(
				"total fetcher " + cityUrlMap.size() + " cities to crawl ");

		fetchers.addAll(this.buildAllFetchers(cityUrlMap));
		Map<String, Collection<?>> resultMap = new HashMap<String, Collection<?>>();

		resultMap.put(FetcherConstants.Fetcher, fetchers);

		return resultMap;
	}

}
