package subway.domain;

import org.springframework.util.StringUtils;
import subway.exception.BusinessException;

public class Station {

    private static final int MAX_STATION_NAME_LENGTH = 20;
    
    private final String name;

    public Station(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new BusinessException("역 이름이 공백입니다. 글자를 입력해주세요");
        }
        if (name.length() > MAX_STATION_NAME_LENGTH) {
            throw new BusinessException("역 이름이 " + MAX_STATION_NAME_LENGTH + "글자를 초과했습니다");
        }
    }

}
