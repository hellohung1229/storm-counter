package wikipedia.topologies;

import wikipedia.bolts.TextFromWikiArticleBolt;
import wikipedia.bolts.WikiArticleGeneratorBolt;
import wikipedia.spouts.WikiDumpReferenceSpout;
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

public final class WikiTopology {
	public static void main(final String[] args) throws Exception {

		final StormTopology topology= createTopology();

		final Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(7);
			StormSubmitter.submitTopology(args[0], conf, topology);
		} else {
			final LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("wiki-topology", conf, topology);
		}
	}

	private static StormTopology createTopology() {
		final TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("wikiDumpReferenceSpout", new WikiDumpReferenceSpout(), 1);

		builder.setBolt("wikiArticleGeneratorBolt", new WikiArticleGeneratorBolt(), 2).shuffleGrouping("wikiDumpReferenceSpout");
		builder.setBolt("textFromWikiArticleBolt", new TextFromWikiArticleBolt(), 2).shuffleGrouping("wikiArticleGeneratorBolt");
		builder.setBolt("textSplitToWordsBolt", new TextSplitToWordsBolt(), 2).shuffleGrouping("textFromWikiArticleBolt");
		builder.setBolt("wordFilterBolt", new WordFilterBolt(), 2).shuffleGrouping("textSplitToWordsBolt");
		builder.setBolt("wordCountBolt", new WordCountBolt(), 2).fieldsGrouping("wordFilterBolt", new Fields("word"));
		builder.setBolt("consolePrintBolt", new ConsolePrintBolt(), 2).shuffleGrouping("wordCountBolt");
		return builder.createTopology();
	}
}
