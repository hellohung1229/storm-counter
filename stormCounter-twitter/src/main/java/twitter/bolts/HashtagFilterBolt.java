package twitter.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class HashtagFilterBolt extends BaseBasicBolt {

	private static final long serialVersionUID = -7805954119445484017L;

	@Override
	public final void execute(Tuple input, BasicOutputCollector collector) {
			String hashtag = input.getValue(0).toString();
			if (validateHashtag(hashtag)) {
				collector.emit(new Values(hashtag.toLowerCase()));
			}
	}

	@Override
	public final void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hashtag"));		
	}
	
	private final boolean validateHashtag(String hashtag) {
		return hashtag.matches("[a-zA-Z0-9]+");
	}
}
