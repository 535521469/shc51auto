package pp.corleone.auto51.service.detail.declaredate

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.bsf.engines.javascript.JavaScriptEngine;
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Component;

import pp.corleone.auto51.domain.Auto51CarInfo
import pp.corleone.auto51.domain.Auto51SellerInfo
import sun.org.mozilla.javascript.internal.NativeArray;

@Component("auto51DetailDeclareDateExtracter")
class Auto51DetailDeclareDateExtracterGroovyImp implements Auto51DetailDeclareDateExtracter {
	
	@Override
	public void fillDeclareDate(Document doc, Auto51CarInfo auto51CarInfo) {
		try {
			String body = doc.body().text();
			String scriptString = body.substring(0,
					body.indexOf("DWREngine._handleResponse"));
			ScriptEngine engine = new ScriptEngineManager()
					.getEngineByExtension("js");
			engine.eval(scriptString);
			NativeArray obj = (NativeArray) engine.get("s0");

			String declareDateString = (String) obj.get(4, obj);

			Date declareDate = new SimpleDateFormat("yyyy-MM-dd")
					.parse(declareDateString);

			auto51CarInfo.setDeclareDate(declareDate);

		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
