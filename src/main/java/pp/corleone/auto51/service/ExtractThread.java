package pp.corleone.auto51.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pp.corleone.Log;
import pp.corleone.service.Callback;
import pp.corleone.service.Fetcher;
import pp.corleone.service.FetcherConstants;

public class ExtractThread extends Thread {

	private <T extends Fetcher> void doFetcher(Collection<T> fs,
			Map<String, Integer> ignored, Map<String, Integer> offered)
			throws InterruptedException {

		for (Fetcher fetcher : fs) {

			String fetcherKey = fetcher.getClass().getName();

			if (fetcher.isIgnore()) {
				Log.debug("ignore fetch "
						+ fetcher.getRequestWrapper().getUrl());
				if (ignored.containsKey(fetcherKey)) {
					ignored.put(fetcherKey, ignored.get(fetcherKey) + 1);
				} else {
					ignored.put(fetcherKey, 1);
				}
				continue;
			}

			boolean offeredFlag = false;
			do {
				offeredFlag = Auto51Resource.fetchQueue.offer(fetcher, 500,
						TimeUnit.MILLISECONDS);
				Log.debug("offer " + fetcher.getRequestWrapper().getUrl());
			} while (!offeredFlag);
			if (offered.containsKey(fetcherKey)) {
				offered.put(fetcherKey, offered.get(fetcherKey) + 1);
			} else {
				offered.put(fetcherKey, 1);
			}
		}
	}

	// private void doStatusRequestWrapper(Collection<StatusRequestWrapper> ss,
	// Map<String, Integer> ignored, Map<String, Integer> offered)
	// throws InterruptedException {
	// for (StatusRequestWrapper srw : ss) {
	// boolean offeredFlag = false;
	// do {
	// offeredFlag = Auto51Resource.statusQueue.offer(srw, 500,
	// TimeUnit.MILLISECONDS);
	// getLogger().debug(
	// "offer "
	// + srw.getFetcher().getRequestWrapper().getUrl()
	// + " delay " + srw.getDelay(TimeUnit.SECONDS)
	// + "seconds");
	// } while (!offeredFlag);
	// }
	// }

	private void logFetched(Map<String, Integer> offered) {
		for (Map.Entry<String, Integer> offer : offered.entrySet()) {
			Log.info("append fetcher task:" + offer.getValue() + " "
					+ offer.getKey());
		}
	}

	private void logIgnored(Map<String, Integer> ignored) {
		for (Map.Entry<String, Integer> ignore : ignored.entrySet()) {
			Log.info("ignore fetcher task:" + ignore.getValue() + " "
					+ ignore.getKey());
		}
	}

	@Override
	public void run() {

		// in extract carry thread
		while (!isInterrupted()) {
			Callback cb = Auto51Resource.extractQueue.poll();

			if (null == cb) {
				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {

					Future<Map<String, Collection<?>>> f = Auto51Resource.threadPool
							.submit(cb);

					if (null == f) {
						Log.info("non callback ...");
						continue;
					}

					Map<String, Collection<?>> result = null;

					try {
						result = f.get(10, TimeUnit.SECONDS);
					} catch (ExecutionException e) {
						e.printStackTrace();
						Log.error("extract error :"
								+ cb.getResponseWrapper().getUrl()
								+ ",extracter:" + cb.getClass().getName());
						continue;
					} catch (TimeoutException e) {
						Log.error("extract time out :"
								+ cb.getResponseWrapper().getUrl(), e);
						continue;
					}

					if (null == result) {
						continue;
					}

					Map<String, Integer> offered = new HashMap<String, Integer>();
					Map<String, Integer> ignored = new HashMap<String, Integer>();

					if (result.containsKey(FetcherConstants.Fetcher)) {

						@SuppressWarnings("unchecked")
						Collection<Fetcher> fs = (Collection<Fetcher>) result
								.get(FetcherConstants.Fetcher);

						if (fs != null) {
							this.<Fetcher> doFetcher(fs, ignored, offered);
						}
					}

					// if (result.containsKey(FetcherConstants.STATUS)) {
					// @SuppressWarnings("unchecked")
					// Collection<StatusRequestWrapper> ss =
					// (Collection<StatusRequestWrapper>) result
					// .get(FetcherConstants.STATUS);
					// if (ss != null) {
					// this.doStatusRequestWrapper(ss, ignored, offered);
					// }
					// }

					this.logFetched(offered);
					this.logIgnored(ignored);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}
	}
}
