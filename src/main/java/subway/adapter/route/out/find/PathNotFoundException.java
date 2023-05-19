package subway.adapter.route.out.find;

import subway.exception.BusinessException;

public class PathNotFoundException extends BusinessException {

    public PathNotFoundException() {
        super("존재하지 않는 경로입니다.");
    }
}
