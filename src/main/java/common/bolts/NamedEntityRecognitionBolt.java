package common.bolts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.TokenNameFinderModel;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class NamedEntityRecognitionBolt extends BaseBasicBolt {

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String inputText = (String) tuple.getValueByField("text");
		InputStream is = new ByteArrayInputStream(inputText.getBytes());
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer ofd) {
		// TODO Auto-generated method stub

	}

}
