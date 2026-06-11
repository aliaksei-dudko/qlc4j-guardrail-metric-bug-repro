package org.example.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("TABLE")
public record TableBlock(
    List<Row> rows
) implements ResponseBlock {}
