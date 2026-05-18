# qlc4j guardrail metric collision — minimal reproducer

Reproduces the double-registration of `guardrail_invoked_total` /
`guardrail_timed_seconds_*` reported in
[quarkus-langchain4j#2465](https://github.com/quarkiverse/quarkus-langchain4j/issues/2465).

## What it contains

- `EchoAgent` — minimal `@RegisterAiService` with one `@OutputGuardrails`.
- `PassThroughGuardrail` — `OutputGuardrail` whose `validate()` always succeeds.
- `DummyChatModelProducer` — in-memory `ChatModel` so no real LLM call happens.
- `ChatResource` — `GET /chat?q=...` to fire one AI-service invocation.
- Prometheus registry on the classpath via
  `quarkus-micrometer-registry-prometheus`.

No tenant config, no Bedrock, no AWS credentials — the only moving parts are
the AI service, the guardrail, and the Micrometer/Prometheus binding.

## Versions

- Quarkus `3.35.3`
- `quarkus-langchain4j-core` `1.9.2` (also reproducible on `1.10.0`)
- JDK 21

## Run

```sh
gradle quarkusDev
```

(Wrapper not committed; use a system Gradle 8.x. To add the wrapper once:
`gradle wrapper --gradle-version 8.10`, then use `./gradlew quarkusDev`.)

Then in another shell:

```sh
curl -s 'http://localhost:8080/chat?q=hi'
curl -s http://localhost:8080/q/metrics | grep guardrail_invoked_total
```

## Expected output (the bug)

In the Quarkus log you should see, on the very first request:

```
WARN  [dev.langchain4j.observability.api.DefaultAiServiceListenerRegistrar]
  An error occurred while firing event (DefaultOutputGuardrailExecutedEvent) ...
  Prometheus requires that all meters with the same name have the same set of tag keys.
  There is already an existing meter named 'guardrail_invoked_total' containing tag keys
  [class, exception, method, result].
  The meter you are attempting to register has keys
  [aiservice, guardrail, guardrail_type, operation, outcome].
```

Confirms:

- Path 1 (legacy `@Counted`/`@Timed` synthesized by
  `GuardrailObservabilityProcessor#transformWithMetrics`) registers the meter
  first with tag keys `[class, method, result, exception]`.
- Path 2 (event-based `GuardrailMetricsObserverSupport`) tries to register the
  same name with `[aiservice, operation, guardrail, guardrail.type, outcome]`
  and is rejected by Prometheus.
- Same applies to `guardrail_timed_seconds_*`.

## Workaround

Add a `MeterFilter` that drops the legacy registration. The `class` tag is
unique to the legacy path:

```java
@Produces
@Singleton
MeterFilter dropLegacyGuardrailMeters() {
    return MeterFilter.deny(id -> {
        var name = id.getName();
        return ("guardrail.invoked".equals(name) || "guardrail.timed".equals(name))
                && id.getTag("class") != null;
    });
}
```

## See also

- Bug write-up: `../orchestration-agent/researches/qlc4j-guardrail-metric-collision.md`
- Upstream issue: https://github.com/quarkiverse/quarkus-langchain4j/issues/2465
- Related upstream PR (deprecated legacy path but did not remove it):
  https://github.com/quarkiverse/quarkus-langchain4j/pull/1984