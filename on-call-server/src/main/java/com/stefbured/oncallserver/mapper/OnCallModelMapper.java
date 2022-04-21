package com.stefbured.oncallserver.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class OnCallModelMapper extends ModelMapper {
    public void mapSkippingNullValues(Object source, Object destination) {
        var isSkipNullEnabledOld = getConfiguration().isSkipNullEnabled();
        var oldMatchingStrategy = getConfiguration().getMatchingStrategy();
        getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
        map(source, destination);
        getConfiguration().setSkipNullEnabled(isSkipNullEnabledOld).setMatchingStrategy(oldMatchingStrategy);
    }
}
