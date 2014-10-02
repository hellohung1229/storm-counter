package twitter.bolts;

import twitter.tools.Rankable;
import twitter.tools.RankableObjectWithFields;
import backtype.storm.tuple.Tuple;

public class IntermediateRankingsBolt extends AbstractRankerBolt {
	private static final long serialVersionUID = -4396975164656779015L;

	public IntermediateRankingsBolt() {
		super();
	}

	public IntermediateRankingsBolt(int topN) {
		super(topN);
	}

	public IntermediateRankingsBolt(int topN, int emitFrequencyInSeconds) {
		super(topN, emitFrequencyInSeconds);
	}

	@Override
	void updateRankingsWithTuple(Tuple tuple) {
		Rankable rankable = RankableObjectWithFields.from(tuple);
		super.getRankings().updateWith(rankable);
	}
}
