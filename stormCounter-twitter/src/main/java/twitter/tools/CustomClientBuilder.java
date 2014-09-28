package twitter.tools;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.params.BasicHttpParams;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.httpclient.BasicClient;
import common.utils.PropertyUtil;

public class CustomClientBuilder extends ClientBuilder {

	@Override
	public BasicClient build() {
		BasicHttpParams params = new BasicHttpParams();
		if ("true".equals(PropertyUtil.getProperty("proxyEnabled"))) {
			HttpHost proxy = new HttpHost(
					PropertyUtil.getProperty("proxyHost"),
					Integer.parseInt(PropertyUtil.getProperty("proxyPort")),
					"http");
			params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		return new BasicClient(name, hosts, endpoint, auth, enableGZip,
				processor, reconnectionManager, rateTracker, executorService,
				eventQueue, params, schemeRegistry);
	}
}
