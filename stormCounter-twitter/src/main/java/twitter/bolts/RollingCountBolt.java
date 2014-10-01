package twitter.bolts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import twitter.tools.NthLastModifiedTimeTracker;
import twitter.tools.SlidingWindowCounter;
import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import common.utils.TupleHelpers;

public class RollingCountBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1283481617195162664L;
	private static final int NUM_WINDOW_CHUNKS = 5;
	private static final int DEFAULT_SLIDING_WINDOW_IN_SECONDS = NUM_WINDOW_CHUNKS * 60;
	private static final int DEFAULT_EMIT_FREQUENCY_IN_SECONDS = DEFAULT_SLIDING_WINDOW_IN_SECONDS / NUM_WINDOW_CHUNKS;
	private final SlidingWindowCounter<Object> counter;
	private final int windowLengthInSeconds;
	private final int emitFrequencyInSeconds;
	private OutputCollector collector;
	private NthLastModifiedTimeTracker lastModifiedTracker;

	public RollingCountBolt() {
		this(DEFAULT_SLIDING_WINDOW_IN_SECONDS, DEFAULT_EMIT_FREQUENCY_IN_SECONDS);
	}

	public RollingCountBolt(int windowLengthInSeconds, int emitFrequencyInSeconds) {
		this.windowLengthInSeconds = windowLengthInSeconds;
		this.emitFrequencyInSeconds = emitFrequencyInSeconds;
		counter = new SlidingWindowCounter<Object>(deriveNumWindowChunksFrom(this.windowLengthInSeconds, this.emitFrequencyInSeconds));
	}

	private int deriveNumWindowChunksFrom(int windowLengthInSeconds, int windowUpdateFrequencyInSeconds) {
		return windowLengthInSeconds / windowUpdateFrequencyInSeconds;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		lastModifiedTracker = new NthLastModifiedTimeTracker(deriveNumWindowChunksFrom(this.windowLengthInSeconds, this.emitFrequencyInSeconds));
	}

	@Override
	public void execute(Tuple tuple) {
		if (TupleHelpers.isTickTuple(tuple)) {
			emitCurrentWindowCounts();
		} else {
			countObjAndAck(tuple);
		}
	}

	private void emitCurrentWindowCounts() {
		Map<Object, Long> counts = counter.getCountsThenAdvanceWindow();
		int actualWindowLengthInSeconds = lastModifiedTracker.secondsSinceOldestModification();
		lastModifiedTracker.markAsModified();
		emit(counts, actualWindowLengthInSeconds);
	}

	private void emit(Map<Object, Long> counts, int actualWindowLengthInSeconds) {
		for (Entry<Object, Long> entry : counts.entrySet()) {
			Object obj = entry.getKey();
			Long count = entry.getValue();
			collector.emit(new Values(obj, count, actualWindowLengthInSeconds));
		}
	}

	private void countObjAndAck(Tuple tuple) {
		Object obj = tuple.getValue(0);
		counter.incrementCount(obj);
		collector.ack(tuple);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("obj", "count", "actualWindowLengthInSeconds"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		Map<String, Object> conf = new HashMap<String, Object>();
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds);
		return conf;
	}
}
