package pp.corleone.auto51.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pp.corleone.Log;
import pp.corleone.service.Callback;
import pp.corleone.service.Fetcher;
import pp.corleone.service.ResponseWrapper;

public class FetcherThread extends Thread {

	@Override
	public void run() {

		ThreadPoolExecutor pe = Auto51Resource.threadPool;

		Fetcher fetcher = null;

		int fetcher_idle_sleep = Auto51Constant.getInstance().getProperty(
				Auto51Constant.FETCHER_IDLE_SLEEP, 30);

		while (!isInterrupted()) {
			try {
				// in fetcher carry thread
				fetcher = Auto51Resource.fetchQueue.poll(fetcher_idle_sleep,
						TimeUnit.SECONDS);
				if (null == fetcher) {
					Log.info("non fetchable ...");
					continue;
				}

				if (fetcher.getRequestWrapper().getReferRequestWrappers()
						.size() > 0) {

					Log.debug("fetch priority "
							+ fetcher.getRequestWrapper().getPriority() + " "
							+ fetcher.getRequestWrapper().getUrl()
							+ " refer to "
							+ fetcher.getRequestWrapper().getLastRequestUrl());
				} else {
					Log.debug("fetch priority "
							+ fetcher.getRequestWrapper().getPriority() + " "
							+ fetcher.getRequestWrapper().getUrl());
				}

				Future<ResponseWrapper> fu = pe.submit(fetcher);

				try {
					ResponseWrapper responseWrapper = fu.get();
					if (null != responseWrapper) {
						Callback cb = fetcher.getRequestWrapper().getCallback();
						cb.setResponseWrapper(responseWrapper);
						boolean offeredFlag = false;

						do {
							offeredFlag = Auto51Resource.extractQueue.offer(cb,
									500, TimeUnit.MILLISECONDS);

							Log.debug("offer callback "
									+ cb.getClass()
									+ " refer to "
									+ cb.getResponseWrapper()
											.getReferRequestWrapper().getUrl());

						} while (!offeredFlag);
					} else {
						Log.debug("ignore fetch :"
								+ fetcher.getRequestWrapper().getUrl());
					}
				} catch (ExecutionException e) {
					Log.error("in FetcherThread run ", e);
					continue;
				}

			} catch (InterruptedException e) {
				Log.error("in FetcherThread run ", e);
			}

			int FETCHER_SLEEP_MILLISECOND = Auto51Constant.getInstance()
					.getProperty(Auto51Constant.FETCHER_SLEEP_MILLISECOND, 0);

			if (FETCHER_SLEEP_MILLISECOND > 0) {

				try {
					TimeUnit.MILLISECONDS.sleep(FETCHER_SLEEP_MILLISECOND);
				} catch (InterruptedException e) {
					Log.error("in FetcherThread run ", e);
				}
			}

		}

	}
}
