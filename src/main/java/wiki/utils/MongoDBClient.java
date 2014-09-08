package wiki.utils;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import common.utils.PropertyParser;

public class MongoDBClient {
	private final String host;
	private final int port;
	private final String databaseName;
	private final String user;
	private final String password;

	public MongoDBClient() {
		this.host = PropertyParser.getProperty("mongoHost");
		this.port = Integer.parseInt(PropertyParser.getProperty("mongoPort"));
		this.databaseName = PropertyParser.getProperty("mongoDatabase");
		this.user = PropertyParser.getProperty("mongoUser");
		this.password = PropertyParser.getProperty("mongoPassword");
	}

	public DB connect() {
		try {
			final MongoClient mongoClient = new MongoClient(host, port);
			final DB database = mongoClient.getDB(this.databaseName);
			final boolean auth = database.authenticate(user, password.toCharArray());
			return database;
		} catch (final UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
}
