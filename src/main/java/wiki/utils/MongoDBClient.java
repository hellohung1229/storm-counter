package wiki.utils;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import common.utils.PropertyUtil;

public class MongoDBClient {
	private final String host;
	private final int port;
	private final String databaseName;
	private final String user;
	private final String password;

	public MongoDBClient() {
		this.host = PropertyUtil.getProperty("mongoHost");
		this.port = Integer.parseInt(PropertyUtil.getProperty("mongoPort"));
		this.databaseName = PropertyUtil.getProperty("mongoDatabase");
		this.user = PropertyUtil.getProperty("mongoUser");
		this.password = PropertyUtil.getProperty("mongoPassword");
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
