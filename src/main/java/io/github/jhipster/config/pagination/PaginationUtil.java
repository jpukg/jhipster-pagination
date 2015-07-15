package io.github.jhipster.config.pagination;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">Github API</api>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 * </p>
 */
public class PaginationUtil {

    public static final int DEFAULT_OFFSET = 1;

    public static final int MIN_OFFSET = 1;

    public static final int DEFAULT_LIMIT = 20;

    public static final int MAX_LIMIT = 100;

    public static HttpHeaders generatePaginationHttpHeaders(Page page, String baseUrl) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotalElements());
        String link = "";
        if ((page.getNumber()+1) < page.getTotalPages()) {
            link = "<" + (new URI(baseUrl +"?page=" + (page.getNumber() + 2) + "&per_page=" + page.getSize())).toString()
                + ">; rel=\"next\",";
        }
        if ((page.getNumber()) > 0) {
            link += "<" + (new URI(baseUrl +"?page=" + (page.getNumber()) + "&per_page=" + page.getSize())).toString()
                + ">; rel=\"prev\",";
        }
        link += "<" + (new URI(baseUrl +"?page=" + page.getTotalPages() + "&per_page=" + page.getSize())).toString()
            + ">; rel=\"last\"," +
            "<" + (new URI(baseUrl +"?page=" + 1 + "&per_page=" + page.getSize())).toString()
            + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }
}
