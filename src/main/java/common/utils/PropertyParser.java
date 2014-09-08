package common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyParser {

	public static String getProperty(final String propertyName) {
		try (InputStream input = PropertyParser.class.getClassLoader().getResourceAsStream("config.properties")) {
			final Properties properties = new Properties();
			properties.load(input);
			return properties.getProperty(propertyName);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
