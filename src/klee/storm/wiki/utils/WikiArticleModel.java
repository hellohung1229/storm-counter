package klee.storm.wiki.utils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WikiArticleModel implements Serializable {
	private String title;
	private int id;
	private String content;

	public WikiArticleModel() {

	}

	public WikiArticleModel(String title, int id, String content) {
		this.title = title;
		this.id = id;
		this.content = content;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {
		return "Title : " + this.title + ", id : " + this.id;
	}
}
