package org.example.block;

import java.util.List;

import dev.langchain4j.model.output.structured.Description;

public record Row(
    List<String> cells
) {
}
