package org.example.block;

import com.fasterxml.jackson.annotation.JsonTypeName;

import dev.langchain4j.model.output.structured.Description;

/**
 * Prose, explanations, and headings rendered as markdown by the client.
 */
@Description("Prose, explanations, headings. Use for any narrative text. Content is markdown.")
@JsonTypeName("TEXT")
public record TextBlock(
    @Description("Markdown text") String content
) implements ResponseBlock {}
