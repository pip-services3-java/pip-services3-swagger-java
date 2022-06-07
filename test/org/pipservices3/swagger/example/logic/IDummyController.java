package org.pipservices3.swagger.example.logic;

import org.pipservices3.swagger.example.data.Dummy;
import org.pipservices3.commons.data.DataPage;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;

public interface IDummyController {
    DataPage<Dummy> getPageByFilter(String correlationId, FilterParams filter, PagingParams paging);
    Dummy getOneById(String correlationId, String id);
    Dummy create(String correlationId, Dummy entity);
    Dummy update(String correlationId, Dummy entity);
    Dummy deleteById(String correlationId, String id);
}
