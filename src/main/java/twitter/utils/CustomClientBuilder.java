package twitter.utils;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.params.BasicHttpParams;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.httpclient.BasicClient;

public class CustomClientBuilder extends ClientBuilder {

	@Override
	public BasicClient build() {
		BasicHttpParams params = new BasicHttpParams();
		
		
		HttpHost proxy = new HttpHost("172.20.0.9", 3128, "http");
		
		params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		return new BasicClient(name, hosts, endpoint, auth, enableGZip, processor, reconnectionManager, rateTracker, executorService, eventQueue, params, schemeRegistry);
	}
}
