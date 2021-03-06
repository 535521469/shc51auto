package pp.corleone.auto51.service;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import pp.corleone.service.Callback;
import pp.corleone.service.Fetcher;

public class Auto51Resource {

	public static ThreadPoolExecutor fetchThreadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3);

	public static ThreadPoolExecutor extractThreadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	// public static ThreadPoolExecutor threadPool = (ThreadPoolExecutor)
	// Executors
	// .newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3);

	public static BlockingQueue<Fetcher> fetchQueue = new PriorityBlockingQueue<Fetcher>(
			512, new Comparator<Fetcher>() {

				@Override
				public int compare(Fetcher f1, Fetcher f2) {
					return f1.getRequestWrapper().getPriority()
							- f2.getRequestWrapper().getPriority();
				}
			});

	public static BlockingQueue<Callback> extractQueue = new PriorityBlockingQueue<Callback>(
			512, new Comparator<Callback>() {

				@Override
				public int compare(Callback f1, Callback f2) {
					return f1.getResponseWrapper().getReferRequestWrapper()
							.getPriority()
							- f2.getResponseWrapper().getReferRequestWrapper()
									.getPriority();
				}
			});

}
