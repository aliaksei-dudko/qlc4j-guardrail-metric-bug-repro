package org.example;

import java.util.function.Supplier;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import io.quarkus.arc.Arc;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * In-memory ChatModel — returns a constant string. Avoids any real LLM call;
 * we only need the AI service to fire and the OutputGuardrail to run so that
 * both metric-registration paths execute.
 */
@ApplicationScoped
public class DummyChatModelProducer  implements Supplier<ChatModel> {

    @Override
    public ChatModel get() {
        return new ChatModel() {
            @Override
            public ChatResponse doChat(ChatRequest request) {
                return ChatResponse.builder()
                    .aiMessage(AiMessage.from("hello"))
                    .build();
            }
        };
    }
}
