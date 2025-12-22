package com.example.ticketsystem.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public class PaginationUtil {
    public static HttpHeaders buildPaginationHeaders(Page<?> page) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        httpHeaders.add("X-Total-Pages", String.valueOf(page.getTotalPages()));
        httpHeaders.add("X-Page-Number", String.valueOf(page.getNumber()));
        httpHeaders.add("X-Page-Size", String.valueOf(page.getSize()));
        return httpHeaders;
    }
}
