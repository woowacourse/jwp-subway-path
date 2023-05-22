package subway.exception;

import org.springframework.http.HttpStatus;

public class CanNotFindPathException extends CustomException {

    public CanNotFindPathException() {
        super("요청한 출발역에서 도착역으로 이동하는 경로를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
