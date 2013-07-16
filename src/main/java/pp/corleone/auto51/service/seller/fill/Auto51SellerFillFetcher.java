package pp.corleone.auto51.service.seller.fill;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pp.corleone.service.DefaultFetcher;
import pp.corleone.service.RequestWrapper;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("auto51SellerFillFetcher")
public class Auto51SellerFillFetcher extends DefaultFetcher {

	public Auto51SellerFillFetcher(RequestWrapper requestWrapper) {
		super(requestWrapper);
	}

	public Auto51SellerFillFetcher() {
	}

}
