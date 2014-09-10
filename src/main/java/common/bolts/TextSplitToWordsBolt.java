package common.bolts;

import java.text.BreakIterator;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TextSplitToWordsBolt extends BaseBasicBolt {

	@Override
	public final void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final String inputText = (String) tuple.getValueByField("text");
		
		final String[] words = splitTextToWords(inputText);
		
		emitWords(collector, words);
	}

	private final String[] splitTextToWords(String inputText) {
		final String[] outputWords = inputText.split("[^a-zA-Z']+");
		return outputWords;
	}
	
	private final void emitWords(final BasicOutputCollector collector, final String[] words) {
		for (final String word : words) {
			collector.emit(new Values(word));
		}
	}
	
	@Override
	public final void declareOutputFields(final OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("word"));
	}

}
