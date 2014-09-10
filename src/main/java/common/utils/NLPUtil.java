package common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
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

	public static final String[] detectSentencesFromRawText(final String inputText) {
		final SentenceModel model = loadSentenceModel("NLP Models/en-sent.bin");
		final SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		return sentenceDetector.sentDetect(inputText);
	}

	public static final String[] detectTokensFromRawText(final String inputText) {
		final TokenizerModel model = loadTokenizerModel("NLP Models/en-token.bin");
		final Tokenizer tokenizer = new TokenizerME(model);
		return tokenizer.tokenize(inputText);
	}

	public static final String[] detectNamedEntitiesFromTokens(final namedEntities entity, final String[] tokens) {
		final TokenNameFinderModel model = loadNameFinderModel("NLP Models/" + entity.toString());
		final NameFinderME nameFinder = new NameFinderME(model);
		final Span[] nameSpans = nameFinder.find(tokens);
		nameFinder.clearAdaptiveData();
		final String[] namedEntities = convertSpansToStrings(nameSpans, tokens);
		return namedEntities;
	}

	private final static InputStream loadModelAsStream(final String modelPath) {
		return NLPUtil.class.getClassLoader().getResourceAsStream(modelPath);
	}

	private final static SentenceModel loadSentenceModel(final String modelPath) {
		try (final InputStream modelIs = loadModelAsStream(modelPath)) {
			final SentenceModel model = new SentenceModel(modelIs);
			return model;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private final static TokenizerModel loadTokenizerModel(final String modelPath) {
		try (final InputStream modelIs = loadModelAsStream(modelPath)) {
			final TokenizerModel model = new TokenizerModel(modelIs);
			return model;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private final static TokenNameFinderModel loadNameFinderModel(final String modelPath) {
		try (final InputStream modelIs = loadModelAsStream(modelPath)) {
			final TokenNameFinderModel model = new TokenNameFinderModel(modelIs);
			return model;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private final static String[] convertSpansToStrings(final Span[] spans, String[] sourceTokens) {
		List<String> result = new ArrayList<String>();
		for (Span span : spans) {
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = span.getStart(); i < span.getEnd(); i++) {
				stringBuffer.append(sourceTokens[i]);
				stringBuffer.append(" ");
			}
			if (stringBuffer.length()>0) {
				stringBuffer.deleteCharAt(stringBuffer.length()-1);
			}
			result.add(stringBuffer.toString());
		}
		return result.toArray(new String[]{});
	}
}
