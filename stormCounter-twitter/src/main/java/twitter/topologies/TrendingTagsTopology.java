package twitter.topologies;

import twitter.bolts.HashtagFilterBolt;
import twitter.bolts.HashtagsExtractorBolt;
import twitter.bolts.IntermediateRankingsBolt;
import twitter.bolts.RollingCountBolt;
import twitter.bolts.TotalRankingsBolt;
import twitter.spouts.TwitterSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

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
		final String twitterSpoutId = "twitterSpout";
		final String hashtagsExtractorId = "hashtagsExtractorBolt";
		final String hashtagsFilterId = "hashtagFilterBolt";
		final String rollingCounterId = "rollingCountBolt";
		final String intermediateRankerId = "intermediateRankingsBolt";
		final String totalRankerId = "totalRankingsBolt";
		final String consolePrinterId = "consolePrintBolt";
		
		builder.setSpout(twitterSpoutId, new TwitterSpout(), 1);
		builder.setBolt(hashtagsExtractorId, new HashtagsExtractorBolt(), 2).shuffleGrouping(twitterSpoutId);
		builder.setBolt(hashtagsFilterId, new HashtagFilterBolt(), 2).shuffleGrouping(hashtagsExtractorId);
		builder.setBolt(rollingCounterId, new RollingCountBolt(), 2).shuffleGrouping(hashtagsFilterId);
		builder.setBolt(intermediateRankerId, new IntermediateRankingsBolt(), 2).fieldsGrouping(rollingCounterId, new Fields("obj"));
		builder.setBolt(totalRankerId, new TotalRankingsBolt(), 1).shuffleGrouping(intermediateRankerId);
		builder.setBolt(consolePrinterId, new ConsolePrintBolt(), 2).shuffleGrouping(totalRankerId);
		return builder.createTopology();
	}
}
