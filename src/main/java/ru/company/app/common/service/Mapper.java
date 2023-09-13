package ru.company.app.common.service;

import java.util.List;

public interface Mapper {

    <S, D> D map(S source, Class<D> destination);

    <S, D> List<D> mapAsList(List<S> source, Class<D> destination);

}
