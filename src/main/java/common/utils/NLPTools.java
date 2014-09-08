package common.utils;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

public class NLPTools {
	public static enum namedEntities {
		PERSONS("en-ner-person.bin");
		
		private String path;
		
		namedEntities(String path) {
			this.path = path;
		}
		
		public String toString() {
			return path;
		}
	}
	
	public static final String[] detectSentences (String inputText) {
		try (final InputStream modelIn = NLPTools.class.getClassLoader().getResourceAsStream("NLP Models/en-sent.bin")) {
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME  sentenceDetector = new SentenceDetectorME (model);
			return sentenceDetector.sentDetect(inputText);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final Span[] detectNamedEntity (namedEntities entity, String[] sentence) {
		try (final InputStream modelIn = NLPTools.class.getClassLoader().getResourceAsStream("NLP Models/" + entity.toString())) {
			TokenNameFinderModel  model = new TokenNameFinderModel (modelIn);
			NameFinderME  nameFinder  = new NameFinderME (model);
			Span[] names = nameFinder.find(sentence);
			nameFinder.clearAdaptiveData();
			return names;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
