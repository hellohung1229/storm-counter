package nlp.topologies;

import nlp.bolts.NamedEntitiesRecognitionBolt;
import nlp.bolts.SentenceSplitBolt;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import common.bolts.ConsolePrintBolt;
import common.bolts.WordCountBolt;
import common.spouts.RandomTextSpout;

public class NLPExperimentationTopology {
	public static void main(final String[] args) throws Exception {

		final StormTopology topology= createTopology();

		final Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(7);
			StormSubmitter.submitTopology(args[0], conf, topology);
		} else {
			final LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("nlp-topology", conf, topology);
		}
	}

	private static StormTopology createTopology() {
		final TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("randomTextSpout", new RandomTextSpout(), 1);
		
		builder.setBolt("sentenceSplitterBolt", new SentenceSplitBolt(), 2).shuffleGrouping("randomTextSpout");
		builder.setBolt("namedEntityRecognitionBolt", new NamedEntitiesRecognitionBolt(),2).shuffleGrouping("sentenceSplitterBolt");
		builder.setBolt("consolePrintBolt", new ConsolePrintBolt(), 2).shuffleGrouping("namedEntityRecognitionBolt");
		return builder.createTopology();
	}
}
