package org.example.block;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(TextBlock.class),
    @JsonSubTypes.Type(TableBlock.class),
})
public interface ResponseBlock {}
