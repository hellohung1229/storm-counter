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
import backtype.storm.utils.Utils;

public class RandomTextSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private List<String> source;

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		source = new ArrayList<String>();
		source.add("After Iraq, Kerry will travel to Saudi Arabia, which has funded groups opposing the Syrian government. The Saudis, while supportive of the US, are fearful that aggressive action against Isis could trigger a backlash among its own Sunni extremists.");
		source.add("In contrast, Obama's failed attempt to seek congressional backing for US air strikes against Syrian government forces last year, the president is seeking to shore up support on Capitol Hill with further briefings for senators planned on Wednesday but has so far refused to give Congress a veto over his latest strategy.");
		source.add("Barack Obama will pledge on Wednesday night to \"degrade and ultimately destroy\" the Islamic State insurgency operating in both Syria and Iraq in an address to the American people expected to herald a significant escalation of the US military role across the region.");
		source.add("The following maps indicate geographical variation in the level of support for a yes vote using data aggregated from polls carried out by Ipsos MORI Scotland between June 2013 through to August 2014. Datazone-level estimates (each datazone is c300 households) are then derived using the relationship between voting intention and the ONS area classification.");
		source.add("Ipsos MORI has shared with us some data which suggests voting intentions in Edinburgh, Glasgow and other major cities. In many areas, Edinburgh is mostly no leaning already, suggesting Cameron may be seeking a more friendly reception than he might meet in a more obviously nationalist neighbourhood. But it means his job there today is probably not to turn people off voting no, rather than win them away from a yes vote.");
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		String sentence = source.get(new Random().nextInt(source.size()));
		collector.emit(new Values(sentence));
		Utils.sleep(50);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("text"));
	}

}
