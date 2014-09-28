package nlp.bolts;

import nlp.utils.NLPUtil;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SentenceSplitBolt extends BaseBasicBolt {

	@Override
	public final void execute(Tuple tuple, BasicOutputCollector collector) {
		String inputText = (String) tuple.getValueByField("text");
		String[] sentences = NLPUtil.detectSentencesFromRawText(inputText);
		for (String sentence : sentences) {
			collector.emit(new Values(sentence));
		}
	}

	@Override
	public final void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("text"));
	}

}
