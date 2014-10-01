package twitter.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TweetModel {
	private final Date creationDate;
	private final int id;
	private final String content;
	private final String[] hashtags;
	private final String[] trends;
	private final String[] userMentions;
	private final String[] urls;
	private final boolean retweeted;

	public TweetModel(final String rawTweet) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) ((JSONArray) jsonParser.parse(rawTweet)).get(0);
		this.creationDate = parseDate((String) jsonObject.get("created_at"));
		this.id = (int) jsonObject.get("id");
		this.content = (String) jsonObject.get("text");
		JSONObject entities = (JSONObject) jsonObject.get("entities");
		this.hashtags = (String[]) ((JSONArray) entities.get("hashtags")).toArray();
		this.trends = (String[]) ((JSONArray) entities.get("trends")).toArray();
		this.userMentions = (String[]) ((JSONArray) entities.get("user_mentions")).toArray();
		this.urls = (String[]) ((JSONArray) entities.get("urls")).toArray();
		this.retweeted = (boolean) jsonObject.get("retweeted");
	}

	public TweetModel(final Date creationDate, final int id, final String content, final String[] hashtags, final String[] trends, final String[] userMentions, final String[] urls, final boolean retweeted) {
		this.creationDate = creationDate;
		this.id = id;
		this.content = content;
		this.hashtags = hashtags;
		this.trends = trends;
		this.userMentions = userMentions;
		this.urls = urls;
		this.retweeted = retweeted;
	}

	public final Date getCreationDate() {
		return this.creationDate;
	}

	public final int getId() {
		return this.id;
	}

	public final String getContent() {
		return this.content;
	}

	public final String[] getHashtags() {
		return this.hashtags;
	}

	public final String[] getTrends() {
		return this.trends;
	}

	public final String[] getUserMentions() {
		return this.userMentions;
	}

	public final String[] getUrls() {
		return this.urls;
	}

	public final boolean getRetweeted() {
		return this.retweeted;
	}

	private final Date parseDate(String input) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
		try {
			return formatter.parse(input);
		} catch (java.text.ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
