package twitter.tools;

public final class TweetHelper {
	public final static String[] extractHashtagsFromTweet(String tweet) {
		final String[] hashtags = tweet.split("(#[^\\s]+)");
		return hashtags;
	}
}
