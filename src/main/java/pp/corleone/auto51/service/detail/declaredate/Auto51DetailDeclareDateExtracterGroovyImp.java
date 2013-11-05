package pp.corleone.auto51.service.detail.declaredate;

import java.util.Calendar;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.nodes.Document;

import pp.corleone.Log;
import pp.corleone.auto51.domain.Auto51CarInfo;
import sun.org.mozilla.javascript.internal.NativeArray;

//import org.apache.bsf.engines.javascript.JavaScriptEngine;

class Auto51DetailDeclareDateExtracterGroovyImp implements
		Auto51DetailDeclareDateExtracter {

	@Override
	public void fillDeclareDate(Document doc, Auto51CarInfo auto51CarInfo) {
		try {

			if (doc == null) {
				Log.info("declare doc is null ");
			}
			if (doc.body() == null) {
				Log.info("declare doc.body is null ");
			}

			String body = doc.body().text();
			String scriptString = body.substring(0,
					body.indexOf("DWREngine._handleResponse"));
			ScriptEngine engine = new ScriptEngineManager()
					.getEngineByExtension("js");
			engine.eval(scriptString);
			NativeArray obj = (NativeArray) engine.get("s0");

			String declareDateString = (String) obj.get(4, obj);

			Calendar c = Calendar.getInstance();

			// Date declareDate = new SimpleDateFormat("yyyy-MM-dd")
			// .parse(declareDateString);

			// qian fa bu
			String intervalString = declareDateString.substring(0,
					declareDateString.indexOf("\u524D\u53D1\u5E03"));

			int uom = -1;
			int value = 0;
			// day
			if (intervalString.indexOf("\u5929") != -1) {
				uom = Calendar.DAY_OF_YEAR;
				value = Integer.valueOf(intervalString.substring(0,
						intervalString.indexOf("\u5929")));
			} else if (intervalString.indexOf("\u6708") != -1) {
				// month
				uom = Calendar.MONTH;
				value = Integer.valueOf(intervalString.substring(0,
						intervalString.indexOf("\u6708")));
			} else if (intervalString.indexOf("\u5E74") != -1) {
				// year
				uom = Calendar.YEAR;
				value = Integer.valueOf(intervalString.substring(0,
						intervalString.indexOf("\u5E74")));
			} else if (intervalString.indexOf("\u5206\u949F") != -1) {
				// minute
				uom = Calendar.MINUTE;
				value = Integer.valueOf(intervalString.substring(0,
						intervalString.indexOf("\u5206\u949F")));
			} else if (intervalString.indexOf("\u5C0F\u65F6") != -1) {
				// hour
				uom = Calendar.HOUR_OF_DAY;
				value = Integer.valueOf(intervalString.substring(0,
						intervalString.indexOf("\u5C0F\u65F6")));
			}

			c.add(uom, 0 - value);

			Log.info(declareDateString + " at " + c.getTime().toString()
					+ "---:" + auto51CarInfo.getCarSourceUrl());

			auto51CarInfo.setDeclareDate(c.getTime());

		} catch (ScriptException e) {
			Log.error("", e);
		}
	}
}
