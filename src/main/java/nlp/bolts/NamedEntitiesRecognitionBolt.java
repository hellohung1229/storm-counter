package nlp.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import common.utils.NLPUtil;

public class NamedEntitiesRecognitionBolt extends BaseBasicBolt {

	@Override
	public final void execute(Tuple tuple, BasicOutputCollector collector) {
		String inputText = (String) tuple.getValueByField("text");
		String[] tokens = NLPUtil.detectTokensFromRawText(inputText);
		String[] namedEntities = NLPUtil.detectNamedEntitiesFromTokens(NLPUtil.namedEntities.PERSONS, tokens);
		emitEntities(collector, namedEntities);
	}

	@Override
	public final void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("text"));
	}

	private final void emitEntities(BasicOutputCollector collector, String[] entities) {
		for (String entity: entities) {
			collector.emit(new Values(entity));
		}
	}
}
