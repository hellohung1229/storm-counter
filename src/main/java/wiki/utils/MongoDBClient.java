package wiki.utils;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBClient {
	private String host;
	private int port;
	private String databaseName;
	private String user;
	private String password;

	public MongoDBClient() {
		this.host = (String) PropertyParser.getProperty("mongoHost");
		this.port = Integer.parseInt(PropertyParser.getProperty("mongoPort"));
		this.databaseName = (String) PropertyParser.getProperty("mongoDatabase");
		this.user = (String) PropertyParser.getProperty("mongoUser");
		this.password = (String) PropertyParser.getProperty("mongoPassword");
	}

	public DB connect() {
		try {
			MongoClient mongoClient = new MongoClient(host, port);
			DB database = mongoClient.getDB(this.databaseName);
			boolean auth = database.authenticate(user, password.toCharArray());
			return database;
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
}
