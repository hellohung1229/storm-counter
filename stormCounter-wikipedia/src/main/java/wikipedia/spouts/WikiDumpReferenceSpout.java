package wikipedia.spouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wikipedia.tools.MongoDBClient;
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
import common.utils.PropertyUtil;

public final class WikiDumpReferenceSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private DBCollection collection;
	private DBCursor cursor;
	private List<DBObject> sentReferences;

	public void nextTuple() {
		this.cursor = collection.find();
		while (this.cursor.hasNext()) {
			final DBObject fileReference = this.cursor.next();
			if (!this.sentReferences.contains(fileReference)) {
				this.collector.emit(new Values(fileReference.get("filePath")));
				this.sentReferences.add(fileReference);
			}
		}
	}

	@Override
	public void open(final Map conf, final TopologyContext context, final SpoutOutputCollector collector) {
		this.collector = collector;
		this.sentReferences = new ArrayList<>();

		final MongoDBClient mongodbClient = new MongoDBClient();
		final DB database = mongodbClient.connect();
		this.collection = database.getCollection(PropertyUtil.getProperty("mongoWikiDumpReferencesCollection"));
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("fileReference"));
	}
}
