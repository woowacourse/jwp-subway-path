package subway.exception;

import org.springframework.http.HttpStatus;

public class SectionHasSameStationsException extends CustomException {

    public SectionHasSameStationsException() {
        super("구간의 시작역과 끝역은 동일할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
