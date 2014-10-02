package twitter.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Tweet {
	private final Date creationDate;
	private final long id;
	private final String content;
	private final String[] hashtags;
	private final String[] trends;
	private final String[] userMentions;
	private final String[] urls;
	private final boolean retweeted;

	public Tweet(final String rawTweet) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(rawTweet);
		this.creationDate = parseDate((String) jsonObject.get("created_at"));
		this.id = (long) jsonObject.get("id");
		this.content = (String) jsonObject.get("text");
		JSONObject entities = (JSONObject) jsonObject.get("entities");
		this.hashtags = parseJsonArray((JSONArray) entities.get("hashtags"), "text");
		this.trends = parseJsonArray((JSONArray) entities.get("trends"), "trend");
		this.userMentions = parseJsonArray((JSONArray) entities.get("user_mentions"), "screen_name");
		this.urls = parseJsonArray((JSONArray) entities.get("urls"), "expanded_url");
		this.retweeted = (boolean) jsonObject.get("retweeted");
	}

	public Tweet(final Date creationDate, final long id, final String content, final String[] hashtags, final String[] trends, final String[] userMentions, final String[] urls,
			final boolean retweeted) {
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

	public final long getId() {
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
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);
		try {
			return formatter.parse(input);
		} catch (java.text.ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private final String[] parseJsonArray(JSONArray jsonArray, String targetField) {
		int length = jsonArray.toArray().length;
		String[] result = new String[length];
		for (int i = 0; i < length; i++) {
			result[i] =((JSONObject) jsonArray.get(i)).get(targetField).toString();
		}
		return result;
	}
}
