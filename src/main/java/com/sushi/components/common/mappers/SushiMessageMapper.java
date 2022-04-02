package com.sushi.components.common.mappers;

import com.sushi.components.common.message.wrappers.HasSushiWrappers;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

public interface SushiMessageMapper<T extends HasSushiWrappers> {

    T from(String request);

    static Map<SushiWrapperField, String> deserialize(String request) {
        String[] splitRequest = request.split("\n");

        return Arrays.stream(splitRequest)
                .takeWhile(not(String::isEmpty))
                .map(l -> l.split(": "))
                .collect(toMap(h -> SushiWrapperField.fromString(h[0]), h -> h[1]));
    }

    static String serialize(Map<SushiWrapperField, String> sushiServingWrappers) {
        return sushiServingWrappers.entrySet()
                .stream()
                .filter(s -> Objects.nonNull(s.getValue()))
                .map(wrapper -> wrapper.getKey().getField() + ": " + wrapper.getValue())
                .collect(Collectors.joining("\n"));
    }
}
