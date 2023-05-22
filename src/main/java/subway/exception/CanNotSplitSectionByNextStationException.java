package subway.exception;

import org.springframework.http.HttpStatus;

public class CanNotSplitSectionByNextStationException extends CustomException {

    public CanNotSplitSectionByNextStationException() {
        super("다음 역 기준으로 구간을 분리할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
