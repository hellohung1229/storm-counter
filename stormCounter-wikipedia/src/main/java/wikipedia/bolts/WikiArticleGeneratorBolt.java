package wikipedia.bolts;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import wikipedia.tools.WikiArticleModel;
import wikipedia.tools.XMLWikiArticleExtractor;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import common.utils.PropertyUtil;

public final class WikiArticleGeneratorBolt extends BaseBasicBolt {
	BasicOutputCollector collector;

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final String fileReference = (String) tuple.getValueByField("fileReference");
		final String filePath = PropertyUtil.getProperty("wikiDumpsFolderPath") + fileReference;

		try {
			final FTPClient ftpClient = new FTPClient();
			try {
				openFTPConnection(ftpClient);
				emitArticlesFromFTPFile(ftpClient, filePath, collector);
			}finally{
				ftpClient.disconnect();
			}
		} catch (NumberFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void openFTPConnection(final FTPClient ftpClient) throws NumberFormatException, IOException {
		ftpClient.connect(PropertyUtil.getProperty("ftpHost"), Integer.parseInt(PropertyUtil.getProperty("ftpPort")));
		ftpClient.login(PropertyUtil.getProperty("ftpUser"), PropertyUtil.getProperty("ftpPassword"));
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	}



	private static void emitArticlesFromFTPFile(final FTPClient ftpClient, final String filePath, final BasicOutputCollector collector) throws IOException {
		try(InputStream compressedStream = ftpClient.retrieveFileStream(filePath)) {
			final BZip2CompressorInputStream decompressedStream = new BZip2CompressorInputStream(compressedStream);
			final XMLWikiArticleExtractor extractor = new XMLWikiArticleExtractor(decompressedStream);
			WikiArticleModel nextArticle = extractor.extractNextArticle();
			while (nextArticle != null) {
				collector.emit(new Values(nextArticle));
				nextArticle = extractor.extractNextArticle();
			}
		}
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("wikiArticle"));
	}
}