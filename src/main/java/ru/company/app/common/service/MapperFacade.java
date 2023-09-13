package ru.company.app.common.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapperFacade implements Mapper {

    private final ModelMapper mapper;

    @Override
    public <S, D> D map(S source, Class<D> destination) {
        return mapper.map(source, destination);
    }

    @Override
    public <S, D> List<D> mapAsList(List<S> source, Class<D> destination) {
        return source.stream()
                .map(e -> mapper.map(e, destination))
                .toList();
    }
}
