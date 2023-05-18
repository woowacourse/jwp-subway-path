package subway.exception;

import org.springframework.http.HttpStatus;

public class CanNotSplitSectionByPrevStationException extends CustomException {

    public CanNotSplitSectionByPrevStationException() {
        super("이전 역 기준으로 분리할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
