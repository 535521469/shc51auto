package pp.corleone.auto51.service.detail

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

import pp.corleone.auto51.domain.Auto51CarInfo

@Component("Auto51DetailDeclareDateUrlBuilder")
class Auto51DetailDeclareDateUrlBuilderGroovyImp implements Auto51DetailDeclareDateUrlBuilder {

	@Override
	public String getCarDeclareDateUrl(Auto51CarInfo auto51CarInfo, Document doc) {

		String carUrl = auto51CarInfo.getCarSourceUrl();
		String carid = carUrl.substring(carUrl.lastIndexOf("/") + 1,
				carUrl.indexOf(".html"));
		StringBuilder url = new StringBuilder();
		url.append(
				"http://www.51auto.com/dwr/exec/CarViewAJAX.getCarInfoNew?callCount=1")
				.append("&c0-scriptName=CarViewAJAX&c0-methodName=getCarInfoNew")
				.append("&c0-id=718_1373617483103&c0-param0=number:")
				.append(carid).append("&xml=true");
		return url.toString();
	}
}
