package org.example;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Trivial OutputGuardrail. Always succeeds — this is enough to fire the bug:
 * qlc4j synthesizes @Counted("guardrail.invoked") and @Timed("guardrail.timed")
 * onto validate(), and at the same time GuardrailMetricsObserverSupport
 * registers the same names with a different tag set.
 */
@ApplicationScoped
public class PassThroughGuardrail implements OutputGuardrail {
    @Override
    public OutputGuardrailResult validate(OutputGuardrailRequest request) {
        return success();
    }
}
