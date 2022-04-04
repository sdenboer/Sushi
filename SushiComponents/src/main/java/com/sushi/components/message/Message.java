package com.sushi.components.message;

import com.sushi.components.message.wrappers.WrapperField;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
public abstract class Message {

    private Map<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);

    protected Message() {
    }

    protected abstract void addOptionalWrappers();

    protected abstract void addMandatoryWrappers();

    protected void addWrapper(WrapperField field, String value) {
        this.wrappers.put(field, value);
    }

    public String toRequest() {
        addMandatoryWrappers();
        addOptionalWrappers();
        return """
                %s
                           
                """.formatted(MessageMapper.serialize(wrappers));
    }
}
