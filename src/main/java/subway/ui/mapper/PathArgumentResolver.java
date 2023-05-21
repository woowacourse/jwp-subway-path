package subway.ui.mapper;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import subway.business.service.dto.SubwayPathRequest;

public class PathArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathRequest.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        System.out.println(webRequest.getAttribute("firstStationId", 0));
        System.out.println(webRequest.getAttribute("lastStationId", 1));
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return new SubwayPathRequest(
                Long.getLong(request.getParameter("firstStationId")),
                Long.getLong(request.getParameter("lastStationId"))
        );
    }
}
