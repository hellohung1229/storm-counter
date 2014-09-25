package twitter.topologies;

import twitter.spouts.TwitterSpout;
import nlp.bolts.NamedEntitiesRecognitionBolt;
import nlp.bolts.SentenceSplitBolt;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import common.bolts.ConsolePrintBolt;

public class SimpleTweetsTopology {
	public static void main(final String[] args) throws Exception {
		final StormTopology topology= createTopology();

		final Config conf = new Config();
		conf.setDebug(false);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(7);
			StormSubmitter.submitTopology(args[0], conf, topology);
		} else {
			final LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("simple-twitter-topology", conf, topology);
		}
	}

	private static StormTopology createTopology() {
		final TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitterSpout", new TwitterSpout(), 1);
		builder.setBolt("consolePrintBolt", new ConsolePrintBolt(), 2).shuffleGrouping("twitterSpout");
		return builder.createTopology();
	}
}
