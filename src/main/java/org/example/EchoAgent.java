package org.example;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService(chatLanguageModelSupplier = DummyChatModelProducer.class)
@ApplicationScoped
public interface EchoAgent {

    @OutputGuardrails(PassThroughGuardrail.class)
    String chat(@UserMessage String message);
}
