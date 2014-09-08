package wiki.spouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wiki.utils.MongoDBClient;
import wiki.utils.PropertyParser;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class WikiDumpReferenceSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private DBCollection collection;
	private DBCursor cursor;
	private List<DBObject> sentReferences;

	public void nextTuple() {
		this.cursor = collection.find();
		while (this.cursor.hasNext()) {
			DBObject fileReference = this.cursor.next();
			if (!this.sentReferences.contains(fileReference)) {
				this.collector.emit(new Values(fileReference));
				this.sentReferences.add(fileReference);
			}
		}
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		this.sentReferences = new ArrayList<DBObject>();

		MongoDBClient mongodbClient = new MongoDBClient();
		DB database = mongodbClient.connect();
		this.collection = database.getCollection(PropertyParser.getProperty("mongoWikiDumpReferencesCollection"));
	}

	public void declareOutputFields(OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("fileReference"));
	}
}
