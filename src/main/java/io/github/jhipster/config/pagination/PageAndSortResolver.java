package io.github.jhipster.config.pagination;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Argument resolver which supports pagination request params.
 *
 * @author Przemek Nowak [przemek.nowak.pl@gmail.com]
 */
public class PageAndSortResolver implements HandlerMethodArgumentResolver {

    private SortHandlerMethodArgumentResolver sortResolver;

    public static final String OFFSET_PARAM_NAME = "page";
    public static final String LIMIT_PARAM_NAME = "per_page";

    public PageAndSortResolver() {
        this.sortResolver = new SortHandlerMethodArgumentResolver();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Integer viewPageNumber = PaginationUtil.DEFAULT_OFFSET;
        Integer limit = PaginationUtil.DEFAULT_LIMIT;

        String viewPageNumberString = webRequest.getParameter(OFFSET_PARAM_NAME);
        if (StringUtils.hasText(viewPageNumberString)) {
            viewPageNumber = Integer.valueOf(viewPageNumberString);
        }
        String limitString = webRequest.getParameter(LIMIT_PARAM_NAME);
        if (StringUtils.hasText(limitString)) {
            limit = Integer.valueOf(limitString);
        }

        if (viewPageNumber < PaginationUtil.MIN_OFFSET) {
            viewPageNumber = PaginationUtil.DEFAULT_OFFSET;
        }
        if (limit > PaginationUtil.MAX_LIMIT) {
            limit = PaginationUtil.MAX_LIMIT;
        }

        Sort sort = sortResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return new PageRequest(viewPageNumber - 1, limit, sort);
    }
}
