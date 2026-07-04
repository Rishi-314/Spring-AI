package com.rishi.ai.Service;


import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;


@Service
public class InjestionService {
    private final VectorStore vectorStore;

    @Value("classpath:docs/VG_Care_Data.pdf")
    private Resource pdfResource;

    public InjestionService(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void injest() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
        List<Document> documents = pdfReader.get();

        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(documents);

        vectorStore.add(chunks);
        System.out.println("Ingested " + chunks.size() + " chunks into vector store.");

    }
}
