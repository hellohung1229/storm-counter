package common.spouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RandomTextSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private List<String> source;
	
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		source = new ArrayList<String>();
		source.add("However, this particular demon quietly captured the screaming printer.");
		source.add("And so, the monster politely painted a screaming book.");
		source.add("Nobody knows why a elephant quietly destroyed a original arm.");
		source.add("Only one robot nervously ate the confusing gardener.");
		source.add("Almost daily, a evil potato politely laughed at the insane pile of biscuits.");
		source.add("Sometimes, the demon screamed at a dancing police car.");
		source.add("Nobody knows why a evil potato nervously spat on the screaming arm.");
		source.add("Almost daily, a zombie nervously laughed at the royal paper.");		
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		String sentence = source.get(new Random().nextInt(source.size()));
		collector.emit(new Values(sentence));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("text"));		
	}

}
