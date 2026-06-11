package org.example;

import java.util.List;

import org.example.block.ResponseBlock;

/**
 * Wire-format wrapper the orchestration agent emits. The blocks list is defensive-copied at construction and is never
 * null afterwards.
 */
public record ChatResponse(List<ResponseBlock> blocks) {}
