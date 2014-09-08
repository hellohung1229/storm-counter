package common.utils;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

public final class NLPUtil {
	public static enum namedEntities {
		PERSONS("en-ner-person.bin");

		private final String path;

		namedEntities(final String path) {
			this.path = path;
		}

		@Override
		public String toString() {
			return path;
		}
	}
	private NLPUtil(){
		//util
	}
	public static final String[] detectSentences (final String inputText) {
		try (final InputStream modelIn = NLPUtil.class.getClassLoader().getResourceAsStream("NLP Models/en-sent.bin")) {
			final SentenceModel model = new SentenceModel(modelIn);
			final SentenceDetectorME  sentenceDetector = new SentenceDetectorME (model);
			return sentenceDetector.sentDetect(inputText);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static final Span[] detectNamedEntity (final namedEntities entity, final String[] sentence) {
		try (final InputStream modelIn = NLPUtil.class.getClassLoader().getResourceAsStream("NLP Models/" + entity.toString())) {
			final TokenNameFinderModel  model = new TokenNameFinderModel (modelIn);
			final NameFinderME  nameFinder  = new NameFinderME (model);
			final Span[] names = nameFinder.find(sentence);
			nameFinder.clearAdaptiveData();
			return names;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
