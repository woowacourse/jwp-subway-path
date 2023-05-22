package subway.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    private static final String REQUEST_LOG_FORMAT = "[REQUEST][%s][%s]";
    private static final String RESPONSE_LOG_FORMAT = "[RESPONSE][STATUS CODE : %d]";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        logger.info(String.format(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestURI()));
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        logger.info(String.format(RESPONSE_LOG_FORMAT, response.getStatus()));
    }
}
