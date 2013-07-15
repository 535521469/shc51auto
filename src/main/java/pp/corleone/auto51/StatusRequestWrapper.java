package pp.corleone.auto51;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import pp.corleone.auto51.domain.Auto51CarInfo;
import pp.corleone.auto51.service.Auto51Constant;
import pp.corleone.service.DefaultRequestWrapper;
import pp.corleone.service.RequestWrapper;

public class StatusRequestWrapper extends DefaultRequestWrapper implements
		Delayed {

	public StatusRequestWrapper() {
	}

	@Override
	public int compareTo(Delayed o) {

		if (o instanceof StatusRequestWrapper) {
			Auto51CarInfo cmp_ici = (Auto51CarInfo) (((RequestWrapper) o)
					.getContext().get(Auto51Constant.CAR_INFO));

			Auto51CarInfo this_ici = (Auto51CarInfo) this.getContext().get(
					Auto51Constant.CAR_INFO);
			long t = this_ici.getLastActiveDate().getTime()
					- cmp_ici.getLastActiveDate().getTime();

			if (0 == t) {
				return 0;
			}

			return t > 0 ? 1 : -1;

		} else {
			throw new IllegalArgumentException(
					"must instance of StatusRequestWrapper , " + o.getClass());
		}

	}

	@Override
	public long getDelay(TimeUnit unit) {
		Auto51CarInfo this_ici = (Auto51CarInfo) this.getContext().get(
				Auto51Constant.CAR_INFO);

		long lastActiveDateTime = this_ici.getLastActiveDate().getTime();

		long now = new Date().getTime();

		long statusDelay = Auto51Constant.getInstance().getProperty(
				Auto51Constant.STATUS_DELAY, 28800);
		int statusAheadOfTime = Auto51Constant.getInstance().getProperty(
				Auto51Constant.AHEAD_OF_TIME, 600);

		long seconds = TimeUnit.SECONDS.convert((lastActiveDateTime - now),
				TimeUnit.MILLISECONDS) + statusDelay - statusAheadOfTime;

		return seconds;

	}
}
