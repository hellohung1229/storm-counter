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
import common.utils.PropertyUtil;

public class WikiArticleGeneratorBolt extends BaseBasicBolt {
	BasicOutputCollector collector;

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final DBObject fileReference = (DBObject) tuple.getValueByField("fileReference");
		final String filePath = PropertyUtil.getProperty("wikiDumpsFolderPath") + fileReference.get("filePath");

		final FTPClient ftpClient = new FTPClient();
		openFTPConnection(ftpClient);
		emitArticlesFromFTPFile(ftpClient, filePath, collector);
		closeFTPConnection(ftpClient);
	}

	private static void openFTPConnection(final FTPClient ftpClient) {
		try {
			ftpClient.connect(PropertyUtil.getProperty("ftpHost"), Integer.parseInt(PropertyUtil.getProperty("ftpPort")));
			ftpClient.login(PropertyUtil.getProperty("ftpUser"), PropertyUtil.getProperty("ftpPassword"));
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (NumberFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void closeFTPConnection(final FTPClient ftpClient) {
		try {
			ftpClient.disconnect();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void emitArticlesFromFTPFile(final FTPClient ftpClient, final String filePath, final BasicOutputCollector collector) {
		try(InputStream compressedStream = ftpClient.retrieveFileStream(filePath)) {
			final BZip2CompressorInputStream decompressedStream = new BZip2CompressorInputStream(compressedStream);
			final XMLWikiArticleExtractor extractor = new XMLWikiArticleExtractor(decompressedStream);
			WikiArticleModel nextArticle = extractor.extractNextArticle();
			while (nextArticle != null) {
				collector.emit(new Values(nextArticle));
				nextArticle = extractor.extractNextArticle();
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("wikiArticle"));
	}
}