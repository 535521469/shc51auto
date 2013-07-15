package pp.corleone.auto51.service.detail;

import org.jsoup.nodes.Document;

import pp.corleone.auto51.domain.Auto51CarInfo;

public interface Auto51DetailExtracter {

	public abstract Auto51CarInfo getCarInfo(Document doc);

	public abstract boolean isOnline(Document doc);
}
