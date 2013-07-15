package pp.corleone.auto51.service.status;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.RequestWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51StatusFetcher")
public class Auto51StatusFetcher extends DefaultFetcher {

	public Auto51StatusFetcher() {
	}

	public Auto51StatusFetcher(RequestWrapper requestWrapper) {
		super(requestWrapper);
	}

}
