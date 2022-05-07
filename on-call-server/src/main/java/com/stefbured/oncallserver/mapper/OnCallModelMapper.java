package com.stefbured.oncallserver.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;

public class OnCallModelMapper extends ModelMapper {
    public void mapSkippingNullValues(Object source, Object destination) {
        var isSkipNullEnabledOld = getConfiguration().isSkipNullEnabled();
        var oldMatchingStrategy = getConfiguration().getMatchingStrategy();
        getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
        map(source, destination);
        getConfiguration().setSkipNullEnabled(isSkipNullEnabledOld).setMatchingStrategy(oldMatchingStrategy);
    }

    public <S, D> Collection<D> mapCollection(Collection<S> source, Class<D> destinationType, String typeMapName) {
        return source.stream()
                .map(s -> map(s, destinationType, typeMapName))
                .toList();
    }
}
