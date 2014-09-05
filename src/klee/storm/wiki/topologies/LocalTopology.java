package klee.storm.wiki.topologies;

import java.util.HashMap;
import java.util.Map;

import klee.storm.wiki.bolts.ConsolePrintBolt;
import klee.storm.wiki.bolts.WikiArticleGeneratorBolt;
import klee.storm.wiki.spouts.WikiDumpReferenceSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class LocalTopology {
	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("wikiDumpReferenceSpout", new WikiDumpReferenceSpout(), 1);

		builder.setBolt("wikiArticleGeneratorBolt", new WikiArticleGeneratorBolt(), 2).shuffleGrouping("wikiDumpReferenceSpout");
		builder.setBolt("consolePrintBolt", new ConsolePrintBolt(), 2).shuffleGrouping("wikiArticleGeneratorBolt");

		Map conf = new HashMap();
		conf.put(Config.TOPOLOGY_WORKERS, 5);
		conf.put(Config.TOPOLOGY_DEBUG, false);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("localTopology", conf, builder.createTopology());
	}
}
