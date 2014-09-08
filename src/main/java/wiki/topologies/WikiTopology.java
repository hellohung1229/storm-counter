package wiki.topologies;

import wiki.bolts.TextFromWikiArticleBolt;
import wiki.bolts.WikiArticleGeneratorBolt;
import wiki.spouts.WikiDumpReferenceSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import common.bolts.ConsolePrintBolt;
import common.bolts.TextSplitToWordsBolt;
import common.bolts.WordCountBolt;
import common.bolts.WordFilterBolt;

public class WikiTopology {
	public static void main(final String[] args) {
		final TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("wikiDumpReferenceSpout", new WikiDumpReferenceSpout(), 1);

		builder.setBolt("wikiArticleGeneratorBolt", new WikiArticleGeneratorBolt(), 2).shuffleGrouping("wikiDumpReferenceSpout");
		builder.setBolt("textFromWikiArticleBolt", new TextFromWikiArticleBolt(), 2).shuffleGrouping("wikiArticleGeneratorBolt");
		builder.setBolt("textSplitToWordsBolt", new TextSplitToWordsBolt(), 2).shuffleGrouping("textFromWikiArticleBolt");
		builder.setBolt("wordFilterBolt", new WordFilterBolt(), 2).shuffleGrouping("textSplitToWordsBolt");
		builder.setBolt("wordCountBolt", new WordCountBolt(), 2).fieldsGrouping("wordFilterBolt", new Fields("word"));
		builder.setBolt("consolePrintBolt", new ConsolePrintBolt(), 2).shuffleGrouping("wordCountBolt");

		final Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(7);
			try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException | InvalidTopologyException e) {
				throw new RuntimeException(e);
			}
		} else {
			final LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("wiki-topology", conf, builder.createTopology());
		}
	}
}
