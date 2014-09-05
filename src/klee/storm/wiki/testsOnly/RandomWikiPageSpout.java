package klee.storm.wiki.testsOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RandomWikiPageSpout extends BaseRichSpout {
	SpoutOutputCollector _collector;
	Random _rand;

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		_collector = collector;
		_rand = new Random();
	}

	public void nextTuple() {
		int maxPageId = 50000000;
		int randomPageId = _rand.nextInt(maxPageId);
		String randomUrl = "http://en.wikipedia.org/w/api.php?format=json&action=query&pageids=" + randomPageId + "&prop=revisions&rvprop=content";
		try {
			JSONObject response = WebClient.readJsonFromUrl(randomUrl).getJSONObject("query").getJSONObject("pages").getJSONObject(String.valueOf(randomPageId));
			if (response.has("title")) {
				String pageTitle = response.getString("title");
				String pageContent = response.getJSONArray("revisions").getJSONObject(0).getString("*");
				_collector.emit(new Values(pageTitle, pageContent), pageTitle);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void ack(Object id) {
	}

	public void fail(Object id) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("pageTitle", "pageContent"));
	}

}
