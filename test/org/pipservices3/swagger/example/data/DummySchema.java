package org.pipservices3.swagger.example.data;

import org.pipservices3.commons.convert.TypeCode;
import org.pipservices3.commons.validate.ObjectSchema;

public class DummySchema extends ObjectSchema {
    public DummySchema() {
        this.withOptionalProperty("id", TypeCode.String);
        this.withRequiredProperty("key", TypeCode.String);
        this.withOptionalProperty("content", TypeCode.String);
    }
}
