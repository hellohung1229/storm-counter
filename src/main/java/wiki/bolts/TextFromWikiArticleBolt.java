package wiki.bolts;

import wiki.utils.WikiArticleModel;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public final class TextFromWikiArticleBolt extends BaseBasicBolt {

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final WikiArticleModel article = (WikiArticleModel) tuple.getValueByField("wikiArticle");
		collector.emit(new Values(article.getContent()));
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("text"));
	}

}
