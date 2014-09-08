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
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final String inputText = (String) tuple.getValueByField("text");
		final InputStream is = new ByteArrayInputStream(inputText.getBytes());
		try {
			final TokenNameFinderModel model = new TokenNameFinderModel(is);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {
		// TODO Auto-generated method stub

	}

}
