package com.stefbured.oncallserver.mapper.util;

import org.modelmapper.internal.MappingContextImpl;

public class OnCallMappingContext<S, D> extends MappingContextImpl<S, D> {
    public OnCallMappingContext(S source) {
        super(source,null, null, null, null, null, null);
    }
}
