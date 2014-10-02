package unitTests;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import twitter.tools.Tweet;

public class TweetModelTest {

	@Test
	public void construct_rawTweet_tweetObject() {
		String rawTweet = "{\"created_at\":\"Wed Oct 01 07:55:48 +0000 2014\",\"id\":517221325090213888,\"text\":\".@UNGeneva Read what #Assyrians in #Iraq thought of President Obama's speechAapoprfwtv #DemandForAction @KinoNuri\",\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[{\"text\":\"Assyrians\",\"indices\":[21,31]},{\"text\":\"Iraq\",\"indices\":[35,40]},{\"text\":\"DemandForAction\",\"indices\":[100,116]}],\"trends\":[],\"urls\":[{\"url\":\"http:Aapoprfwtv\",\"expanded_url\":\"http:8dWr1n\",\"display_url\":\"goo.glWr1n\",\"indices\":[77,99]}],\"user_mentions\":[{\"screen_name\":\"UNGeneva\",\"name\":\"UN Geneva\",\"id\":164263382,\"id_str\":\"164263382\",\"indices\":[1,10]},{\"screen_name\":\"KinoNuri\",\"name\":\"Nuri Kino\",\"id\":1685803026,\"id_str\":\"1685803026\",\"indices\":[117,126]}],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"filter_level\":\"medium\",\"lang\":\"en\",\"timestamp_ms\":\"1412150148071\"}";
		
		try {
			Tweet tweet = new Tweet(rawTweet);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
