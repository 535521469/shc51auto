package pp.corleone.auto51.service.changecity;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.DefaultRequestWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51ChangeCityFetcher")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Auto51ChangeCityFetcher extends DefaultFetcher {

	public Auto51ChangeCityFetcher() {
	}

	public Auto51ChangeCityFetcher(DefaultRequestWrapper requestWrapper) {
		super(requestWrapper);
	}

}
