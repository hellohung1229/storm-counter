package klee.storm.wiki.testsOnly;

import java.io.FileInputStream;
import java.util.Map;

import klee.storm.wiki.utils.WikiArticleModel;
import klee.storm.wiki.utils.XMLWikiArticleExtractor;

import org.itadaki.bzip2.BZip2InputStream;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class BackupWikiSpout extends BaseRichSpout {
	private String _path;
	private XMLWikiArticleExtractor _extractor;
	private SpoutOutputCollector _collector;
	private boolean _active;

	public BackupWikiSpout(String path) {
		this._path = path;
		this._active = true;
	}

	public void nextTuple() {
		String extract;
		// if (_active) {
		// if ((extract = _extractor.nextArticle()) != null) {
		// WikiArticleModel article = new WikiArticleModel(extract);
		// _collector.emit(new Values(article), article.getId());
		// } else {
		// _active = false;
		// System.out.println("File checked !");
		// }
		// }
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		_collector = collector;
		FileInputStream fileInputStream;
		BZip2InputStream decompressedStream;
		try {
			fileInputStream = new FileInputStream(_path);
			decompressedStream = new BZip2InputStream(fileInputStream, false);
			_extractor = new XMLWikiArticleExtractor(decompressedStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("wikiArticle"));
	}

}
