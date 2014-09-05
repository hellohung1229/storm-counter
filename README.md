#Storm project

##Running
To run the topology in local mode, run the `main` method from the `LocalTopoly` class.
##Current topologies
-**`LocalTopoly`**: made of one spout (`WikiDumpReferenceSpout`) and two bolts (`WikiArticleGeneratorBolt`, `ConsolePrintBolt`). The `WikiDumpReferenceSpout` spout watches a mongoDB collection for changes and emits a tuple every type a document is added or updated from the collection. The `WikiArticleGeneratorBolt` bolt reads this entry and loads the remote FTP file described by its `filePath` on the FTP server. It reads through this file and emits a `WikiArticleModel` every time a wiki article page structure is read. The`ConsolePrintBolt` bolt then prints out these tuples to the console.**Warning !** The files stored on the FTP must be Wikipedia Dump formatted as XML and compressed in `.bz2` files.
