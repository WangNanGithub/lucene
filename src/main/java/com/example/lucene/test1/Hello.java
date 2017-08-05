/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with haitouhui.com.
 */
package com.example.lucene.test1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author liuchaoyang, chaoyang.liu@gmail.com
 * @version 1.0
 */
public class Hello {

    public static void main(String[] args) throws Exception {
        /**
         * 保存索引和查找索引的 文件路径和分词器应该是一样的
         */
        // 保存在磁盘中
//        Path path = Paths.get("");
//        Directory directory = FSDirectory.open(path);
        // 保存在内存中
        Directory directory = new RAMDirectory();
        // 分词器
        Analyzer analyzer = new StandardAnalyzer();

        /**
         * 保存索引文件
         */
        /* 索引文件存放位置 */
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, writerConfig);
        Document doc = new Document();
        String text = "This is the text to be indexed.";
        doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();


        /**
         * 查找索引文件
         */
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        QueryParser parser = new QueryParser("fieldname", analyzer);
        Query query = parser.parse("text");
        ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;
        System.out.println(hits.length); // 1
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get("fieldname")); // This is the text to be indexed.
        }
        ireader.close();
        directory.close();
    }

}
