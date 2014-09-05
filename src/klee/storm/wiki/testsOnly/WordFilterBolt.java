package klee.storm.wiki.testsOnly;

import klee.storm.wiki.utils.WikiArticleModel;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordFilterBolt extends BaseBasicBolt {

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		WikiArticleModel article = (WikiArticleModel) tuple.getValueByField("wikiArticle");

		if (article.getContent().matches(".* United States .*")) {
			collector.emit(new Values(article));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("wikiArticle"));
	}

}
