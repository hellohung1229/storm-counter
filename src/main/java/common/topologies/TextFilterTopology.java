package common.topologies;

import wiki.bolts.TextFromWikiArticleBolt;
import wiki.bolts.WikiArticleGeneratorBolt;
import wiki.spouts.WikiDumpReferenceSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import common.bolts.ConsolePrintBolt;
import common.bolts.TextSplitToWordsBolt;
import common.bolts.WordCountBolt;
import common.bolts.WordFilterBolt;
import common.spouts.RandomTextSpout;

public class TextFilterTopology {
	public static void main(final String[] args) throws Exception {

		final StormTopology topology= createTopology();

		final Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(7);
			StormSubmitter.submitTopology(args[0], conf, topology);
		} else {
			final LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("textFilter-topology", conf, topology);
		}
	}

	private static StormTopology createTopology() {
		final TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("randomTextSpout", new RandomTextSpout(), 1);

		builder.setBolt("textSplitToWordsBolt", new TextSplitToWordsBolt(), 2).shuffleGrouping("randomTextSpout");
		builder.setBolt("wordFilterBolt", new WordFilterBolt(), 2).shuffleGrouping("textSplitToWordsBolt");
		builder.setBolt("wordCountBolt", new WordCountBolt(), 2).fieldsGrouping("wordFilterBolt", new Fields("word"));
		builder.setBolt("consolePrintBolt", new ConsolePrintBolt(), 2).shuffleGrouping("wordCountBolt");
		return builder.createTopology();
	}
}
