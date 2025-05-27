package com.framework.listeners;

import com.framework.utils.logger.TestLogger;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CustomRestAssuredFilter implements Filter {
    private static final TestLogger log = new TestLogger(CustomRestAssuredFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        log.info("API REQUEST: {} {}", requestSpec.getMethod(), requestSpec.getURI());
        if (requestSpec.getHeaders() != null && !requestSpec.getHeaders().asList().isEmpty()) {
            log.debug("Headers: {}", requestSpec.getHeaders());
        }
        if (requestSpec.getBody() != null) {
            log.debug("Body: {}", requestSpec.getBody());
        }

        Response response = ctx.next(requestSpec, responseSpec);

        log.info("API RESPONSE: {} {}", String.valueOf(response.getStatusCode()), response.getStatusLine());
        if (response.getHeaders() != null && !response.getHeaders().asList().isEmpty()) {
            log.debug("Response Headers: {}", response.getHeaders());
        }
        if (response.getBody() != null) {
            log.debug("Response Body: {}", response.getBody().asPrettyString());
        }
        return response;
    }
}

