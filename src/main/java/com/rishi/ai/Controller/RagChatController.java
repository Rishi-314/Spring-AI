package com.rishi.ai.Controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagChatController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }
    @GetMapping("/ragchat")
    public String chat(@RequestParam String message) {
        // 1. Retrieve relevant chunks from the vector store
        SearchRequest searchRequest = SearchRequest.builder()
                                                   .query(message)
                                                   .topK(3)
                                                   .build();

        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
        
        String context = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // 2. Build an augmented prompt with retrieved context
        String promptText = """
                Answer the question based only on the context provided below.
                If the answer isn't in the context, say you don't know.

                Context:
                %s

                Question: %s
                """.formatted(context, message);

        // 3. Call the chat model with the augmented prompt
        return chatClient.prompt()
                .user(promptText)
                .call()
                .content();
    }

}