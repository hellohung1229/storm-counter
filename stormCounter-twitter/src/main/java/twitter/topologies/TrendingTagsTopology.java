package twitter.topologies;

import twitter.bolts.HashtagsExtractorBolt;
import twitter.bolts.RollingCountBolt;
import twitter.spouts.TwitterSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import common.bolts.ConsolePrintBolt;

public class TrendingTagsTopology {
	public static void main(final String[] args) throws Exception {
		final StormTopology topology= createTopology();

		final Config conf = new Config();
		conf.setDebug(false);

		if (args != null && args.length > 0) {
			StormSubmitter.submitTopology(args[0], conf, topology);
		} else {
			final LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("twitter-trending-tags-topology", conf, topology);
		}
	}

	private static StormTopology createTopology() {
		final TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitterSpout", new TwitterSpout(), 1);
		builder.setBolt("hashtagsExtractorBolt", new HashtagsExtractorBolt(), 2).shuffleGrouping("twitterSpout");
		builder.setBolt("rollingCountBolt", new RollingCountBolt(), 1).shuffleGrouping("hashtagsExtractorBolt");
		builder.setBolt("consolePrintBolt", new ConsolePrintBolt(), 2).shuffleGrouping("rollingCountBolt");
		return builder.createTopology();
	}
}
