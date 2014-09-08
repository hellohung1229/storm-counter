package common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtil {
	private PropertyUtil(){
		//Util
	}

	public static String getProperty(final String propertyName) {
		try (InputStream input = PropertyUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
			final Properties properties = new Properties();
			properties.load(input);
			return properties.getProperty(propertyName);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
