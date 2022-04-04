package com.sushi.components.message;

import com.sushi.components.message.wrappers.WrapperField;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

public interface MessageMapper<T extends Message> {

    static Map<WrapperField, String> deserialize(String request) {
        String[] splitRequest = request.split("\n");

        return Arrays.stream(splitRequest)
                .takeWhile(not(String::isEmpty))
                .map(l -> l.split(": "))
                .collect(toMap(h -> WrapperField.fromString(h[0]), h -> h[1]));
    }

    static String serialize(Map<WrapperField, String> servingWrappers) {
        return servingWrappers.entrySet()
                .stream()
                .filter(s -> Objects.nonNull(s.getValue()))
                .map(wrapper -> wrapper.getKey().getField() + ": " + wrapper.getValue())
                .collect(Collectors.joining("\n"));
    }

    static String getStringWrapper(Map<WrapperField, String> wrappers, WrapperField field) {
        return wrappers.getOrDefault(field, null);
    }

    static Long getLongWrapper(Map<WrapperField, String> wrappers, WrapperField field) {
        return Optional.ofNullable(wrappers.get(field)).map(Long::parseLong).orElse(null);
    }

    static Integer getIntegerWrapper(Map<WrapperField, String> wrappers, WrapperField field) {
        return Optional.ofNullable(wrappers.get(field)).map(Integer::parseInt).orElse(null);
    }

    T from(String request);
}
