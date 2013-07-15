package pp.corleone.auto51.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pp.corleone.auto51.StatusRequestWrapper;
import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.domain.Auto51CarInfo.Auto51StatusCode;
import pp.corleone.auto51.service.changecity.Auto51ChangeCityCallback;
import pp.corleone.auto51.service.status.Auto51OnlineQuery;
import pp.corleone.auto51.service.status.Auto51StatusCallback;
import pp.corleone.service.Callback;
import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.DefaultRequestWrapper;
import pp.corleone.service.DefaultRequestWrapper.PriorityEnum;
import pp.corleone.service.Fetcher;
import pp.corleone.service.Service;

public class Auto51Service extends Service {

	public Auto51Service() {
	}

	public static void main(String[] args) throws IOException {

		Auto51Constant.getInstance();

		Auto51Service is = new Auto51Service();
		is.getLogger().info("start-------------------");
		String statusCheckFlag = Auto51Constant.getInstance().getProperty(
				Auto51Constant.STATUS_CHECK_FLAG, "1");
		String fetchFlag = Auto51Constant.getInstance().getProperty(
				Auto51Constant.ONGOING_FLAG, "1");
		 if ("1".equals(fetchFlag)) {
		 is.initOnGoing();
		 }
		if ("1".equals(statusCheckFlag)) {
			is.initStatusCheck();
			is.statusFetch();
		}
		is.fetch();
		is.extract();

	}

	public void initOnGoing() {
		ScheduledThreadPoolExecutor pe = (ScheduledThreadPoolExecutor) Executors
				.newScheduledThreadPool(1);
		ChangeCityFetcherManager ccf = this.new ChangeCityFetcherManager();
		long ongoingCycleDelay = Long.valueOf(Auto51Constant.getInstance()
				.getProperty(Auto51Constant.ONGOING_CYCLE_DELAY, 86400));
		pe.scheduleAtFixedRate(ccf, 0, ongoingCycleDelay, TimeUnit.SECONDS);
	}

	public void initStatusCheck() {
		ScheduledThreadPoolExecutor pe = (ScheduledThreadPoolExecutor) Executors
				.newScheduledThreadPool(1);
		StatusFetcherManager sfm = this.new StatusFetcherManager();
		// StatusFetcherManager sfm = (StatusFetcherManager) Auto51Constant
		// .getInstance().getBean("statusFetcherManager");
		pe.scheduleAtFixedRate(sfm, 0, sfm.getStatusCheckRange(),
				TimeUnit.SECONDS);
	}

	@Override
	public void fetch() {
		Thread fetcher = new FetcherThread();
		fetcher.start();
	}

	public void statusFetch() {
		Thread status = new StatusThread();
		status.start();
	}

	@Override
	public void extract() {
		Thread extract = new ExtractThread();
		extract.start();
	}

	class ChangeCityFetcherManager implements Runnable {

		public ChangeCityFetcherManager() {
		}

		protected final Logger getLogger() {
			return LoggerFactory.getLogger(this.getClass());
		}

		@Override
		public void run() {

			String city = Auto51Constant.getInstance().getProperty(
					Auto51Constant.CITIES);

			this.getLogger().info("get cities config ->" + city);
			Set<String> cities = new HashSet<String>(Arrays.asList(city
					.split(",")));
			List<DefaultFetcher> fs = new ArrayList<DefaultFetcher>();

			// add change city fetcher
			try {
				Auto51ChangeCityCallback callback = (Auto51ChangeCityCallback) Auto51Constant
						.getInstance().getBean("auto51ChangeCityCallback");
				callback.setCities(cities);

				DefaultRequestWrapper drw = (DefaultRequestWrapper) Auto51Constant
						.getInstance().getBean("defaultRequestWrapper");
				drw.setUrl(Auto51Constant.homePage + "morecity.htm");
				drw.setCallback(callback);
				drw.setPriority(PriorityEnum.CHANGE_CITY.getValue());
				drw.setTimeout(30000);

				DefaultFetcher f = (DefaultFetcher) Auto51Constant
						.getInstance().getBean("auto51ChangeCityFetcher");
				f.setRequestWrapper(drw);
				fs.add(f);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// add to queue
			Map<String, Integer> offered = new HashMap<String, Integer>();
			for (Object o : fs) {
				Fetcher fetcher = (Fetcher) o;
				String fetcherKey = fetcher.getClass().getName();
				boolean offeredFlag = false;
				do {

					try {
						offeredFlag = Auto51Resource.fetchQueue.offer(fetcher,
								500, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					getLogger().debug(
							"offer " + fetcher.getRequestWrapper().getUrl());

				} while (!offeredFlag);

				if (offered.containsKey(fetcherKey)) {
					offered.put(fetcherKey, offered.get(fetcherKey) + 1);
				} else {
					offered.put(fetcherKey, 1);
				}
			}

			for (Map.Entry<String, Integer> offer : offered.entrySet()) {
				getLogger().info(
						"add " + offer.getValue() + " fetcher task :"
								+ offer.getKey());
			}

		}
	}

	public class StatusFetcherManager implements Runnable {

		public StatusFetcherManager() {
		}

		protected final Logger getLogger() {
			return LoggerFactory.getLogger(this.getClass());
		}

		int statusCheckRange;
		int statusDelay;

		public int getStatusCheckRange() {
			return statusCheckRange;
		}

		public void setStatusCheckRange(int statusCheckRange) {
			this.statusCheckRange = statusCheckRange;
		}

		public int getStatusDelay() {
			return statusDelay;
		}

		public void setStatusDelay(int statusDelay) {
			this.statusDelay = statusDelay;
		}

		{

			statusCheckRange = Auto51Constant.getInstance().getProperty(
					Auto51Constant.STATUS_CHECK_RANGE,
					Runtime.getRuntime().availableProcessors());

			statusDelay = Auto51Constant.getInstance().getProperty(
					Auto51Constant.STATUS_DELAY, 28800);

		}

		@Override
		public void run() {

			this.getLogger()
					.info("...... query carinfo for status check......");

			DateTime dateTime = new DateTime();
			if (this.getStatusCheckRange() > 1) {
				dateTime = dateTime.minusSeconds(this.getStatusDelay());
				dateTime = dateTime.plusSeconds(this.getStatusCheckRange());
			}

			this.getLogger().info(
					"...... before last active date time:"
							+ dateTime.toString("yyyy-MM-dd HH:mm:ss"));

			List<Auto51CarInfo> auto51CarInfos = null;
			Auto51OnlineQuery auto51OnlineFetcher = (Auto51OnlineQuery) Auto51Constant
					.getInstance().getBean("auto51OnlineQuery");

			auto51CarInfos = auto51OnlineFetcher
					.listByStatusCodeAndLastActiveDateTime(
							Auto51StatusCode.STATUS_TYPE_FOR_SALE.getCode(),
							dateTime.toDate());

			getLogger().info(
					"get " + auto51CarInfos.size() + " online car infos");

			List<Fetcher> fetchers = new ArrayList<Fetcher>();

			if (null != auto51CarInfos) {
				for (Auto51CarInfo auto51CarInfo : auto51CarInfos) {
					StatusRequestWrapper statusRequestWrapper = new StatusRequestWrapper();

					Callback callback = (Auto51StatusCallback) Auto51Constant
							.getInstance().getBean("auto51StatusCallback");
					statusRequestWrapper.setCallback(callback);
					statusRequestWrapper.setPriority(PriorityEnum.STATUS
							.getValue());
					statusRequestWrapper.setTimeout(5000);
					statusRequestWrapper
							.setUrl(auto51CarInfo.getCarSourceUrl());
					statusRequestWrapper.getContext().put(
							Auto51Constant.CAR_INFO, auto51CarInfo);

					DefaultFetcher fetcher = (DefaultFetcher) Auto51Constant
							.getInstance().getBean("auto51StatusFetcher");

					fetcher.setRequestWrapper(statusRequestWrapper);
					fetchers.add(fetcher);
				}

			}

			Auto51Resource.fetchQueue.addAll(fetchers);

			getLogger().info("add "+fetchers.size()+" to queue");
			
		}
	}
}
