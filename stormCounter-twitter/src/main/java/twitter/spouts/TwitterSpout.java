package twitter.spouts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.parser.ParseException;

import twitter.tools.CustomClientBuilder;
import twitter.tools.Tweet;
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

import common.utils.PropertyUtil;

public class TwitterSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
	private Client hosebirdClient;

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		List<String> terms = Lists.newArrayList("#iraq");
		hosebirdEndpoint.trackTerms(terms);
		Authentication hosebirdAuth = new OAuth1(PropertyUtil.getProperty("twitterAPIKey"), PropertyUtil.getProperty("twitterAPISecret"), PropertyUtil.getProperty("twitterAccessToken"), PropertyUtil.getProperty("twitterAccessTokenSecret"));
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
				Tweet tweet = new Tweet(msg);
				collector.emit(new Values(tweet));
			} catch (final InterruptedException | ParseException e) {
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
