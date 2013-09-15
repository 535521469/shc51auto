package pp.corleone.auto51.service;

import java.util.concurrent.TimeUnit;

import pp.corleone.Log;
import pp.corleone.service.Fetcher;

public class StatusThread extends Thread {

	@Override
	public void run() {

		Fetcher fetcher = null;
		int status_carrier_idle_sleep = Auto51Constant.getInstance()
				.getProperty(
						Auto51Constant.STATUS_CARRIER_IDLE_SLEEP,
						Auto51Constant.getInstance().getProperty(
								Auto51Constant.AHEAD_OF_TIME, 300)
								/ Runtime.getRuntime().availableProcessors());

		while (!isInterrupted()) {

			// in status carry thread
			try {
				fetcher = Auto51Resource.fetchQueue.poll(
						status_carrier_idle_sleep, TimeUnit.SECONDS);

				if (null == fetcher) {
					Log.info(
							"not reach check time, queue size :"
									+ Auto51Resource.fetchQueue.size());
					continue;
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			boolean offered = false;

			do {
				try {
					offered = Auto51Resource.fetchQueue.offer(fetcher, 5,
							TimeUnit.SECONDS);

					Log.debug(
							"offer status fetcher "
									+ fetcher.getRequestWrapper()
											.getUrl());

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!offered);

		}

	}
}
