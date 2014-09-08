package wiki.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLWikiArticleExtractor {
	private final BufferedReader reader;

	public XMLWikiArticleExtractor(final InputStream stream) {
		this.reader = new BufferedReader(new InputStreamReader(stream));
	}

	public WikiArticleModel extractNextArticle() {
		final WikiArticleModel article = new WikiArticleModel();

		final SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException(e);
		}

		final DefaultHandler handler = new DefaultHandler() {
			boolean bTitle;
			boolean bId ;
			boolean bPageId ;
			boolean bContent;

			@Override
			public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
				if (qName.equalsIgnoreCase("title")) {
					bTitle = true;
				}
				if (qName.equalsIgnoreCase("id") && bPageId) {
					bId = true;
				}
				if (qName.equalsIgnoreCase("page")) {
					bPageId = true;
				}
				if (qName.equalsIgnoreCase("revision")) {
					bPageId = false;
				}
				if (qName.contains("text")) {
					bContent = true;
				}
			}

			@Override
			public void endElement(final String uri, final String localName, final String qName) throws SAXException {
			}

			@Override
			public void characters(final char ch[], final int start, final int length) throws SAXException {

				if (bTitle) {
					article.setTitle(new String(ch, start, length));
					bTitle = false;
				}
				if (bId) {
					article.setId(Integer.parseInt(new String(ch, start, length)));
					bId = false;
				}
				if (bContent) {
					article.setContent(new String(ch, start, length));
					bContent = false;
				}
			}
		};
		try {
			final String source = extractNextArticleSource();
			if (source != null) {
				saxParser.parse(new InputSource(new StringReader(source)), handler);
			}
		} catch (SAXException | IOException e) {
			throw new RuntimeException(e);
		}
		if (article.getTitle() != null) {
			return article;
		} else {
			return null;
		}

	}

	private final String extractNextArticleSource() {
		try {
			return doExtractNextArticleSource();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private final String doExtractNextArticleSource() throws IOException {
		String line;
		String partialSource = "";
		Boolean articleStarted = false;

		while ((line = this.reader.readLine()) != null) {
			if (line.contains("<page>")) {
				articleStarted = true;
			}
			if (articleStarted) {
				partialSource += line;
			}
			if (line.contains("</page>")) {
				articleStarted = false;
				return partialSource;
			}
		}
		return null; // End of the file, incomplete article source (stopped
		// before </page>)
	}
}
