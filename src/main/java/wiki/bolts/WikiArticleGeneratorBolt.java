package wiki.bolts;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import wiki.utils.WikiArticleModel;
import wiki.utils.XMLWikiArticleExtractor;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.mongodb.DBObject;
import common.utils.PropertyParser;

public class WikiArticleGeneratorBolt extends BaseBasicBolt {
	BasicOutputCollector collector;

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		DBObject fileReference = (DBObject) tuple.getValueByField("fileReference");
		String filePath = PropertyParser.getProperty("wikiDumpsFolderPath") + fileReference.get("filePath");

		final FTPClient ftpClient = new FTPClient();
		openFTPConnection(ftpClient);
		emitArticlesFromFTPFile(ftpClient, filePath, collector);
		closeFTPConnection(ftpClient);
	}

	private static void openFTPConnection(FTPClient ftpClient) {
		try {
			ftpClient.connect(PropertyParser.getProperty("ftpHost"), Integer.parseInt(PropertyParser.getProperty("ftpPort")));
			ftpClient.login(PropertyParser.getProperty("ftpUser"), PropertyParser.getProperty("ftpPassword"));
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (NumberFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void closeFTPConnection(FTPClient ftpClient) {
		try {
			ftpClient.disconnect();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void emitArticlesFromFTPFile(FTPClient ftpClient, String filePath, BasicOutputCollector collector) {
		try(InputStream compressedStream = ftpClient.retrieveFileStream(filePath)) {
			BZip2CompressorInputStream decompressedStream = new BZip2CompressorInputStream(compressedStream);
			XMLWikiArticleExtractor extractor = new XMLWikiArticleExtractor(decompressedStream);
			WikiArticleModel nextArticle = extractor.extractNextArticle();
			while (nextArticle != null) {
				collector.emit(new Values(nextArticle));
				nextArticle = extractor.extractNextArticle();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("wikiArticle"));
	}
}