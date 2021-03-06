package twitter.bolts;

import twitter.tools.Tweet;
import twitter.tools.TweetHelper;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class HashtagsExtractorBolt extends BaseBasicBolt {

	private static final long serialVersionUID = -7941743019270748812L;

	@Override
	public final void execute(Tuple input, BasicOutputCollector collector) {
		final Tweet tweet = (Tweet) input.getValue(0);
		for (String hastag : tweet.getHashtags()) {
			collector.emit(new Values(hastag));
		}
	}

	@Override
	public final void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hashtag"));
	}

}
