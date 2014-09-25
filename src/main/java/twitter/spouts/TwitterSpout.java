package twitter.spouts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import twitter.utils.CustomClientBuilder;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
	private Client hosebirdClient;

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		List<String> terms = Lists.newArrayList("#syria");
		hosebirdEndpoint.trackTerms(terms);
		Authentication hosebirdAuth = new OAuth1("MsedQrbMlzfR5mueGdACwfUa8", "fYBzpbQLc5VUcMPPVgraOpiJ0O0HKPPOsyOLkYu2jGTxVcjvDE", "2831794855-pFHf7dRW8LNmERxlDONSeUangF9axJu4ILLGSuC", "lGW39jhCGC22x3YjPzC9QxzJoH8E8pqdzR6H19T4SsioV");
		ClientBuilder builder = new CustomClientBuilder().hosts(hosebirdHosts).authentication(hosebirdAuth).endpoint(hosebirdEndpoint).processor(new StringDelimitedProcessor(msgQueue));

		hosebirdClient = builder.build();
		// Attempts to establish a connection.
		hosebirdClient.connect();
	}

	@Override
	public void nextTuple() {
		if (!hosebirdClient.isDone()) {
			try {
				String msg = msgQueue.take();
				collector.emit(new Values(msg));
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("text"));
	}

	@Override
	public void close() {
		hosebirdClient.stop();
	}

}
