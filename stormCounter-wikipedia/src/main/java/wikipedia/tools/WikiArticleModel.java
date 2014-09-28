package wikipedia.tools;

import java.io.Serializable;

public final class WikiArticleModel implements Serializable {
	private String title;
	private int id;
	private String content;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Title : " + this.title + ", id : " + this.id;
	}
}
