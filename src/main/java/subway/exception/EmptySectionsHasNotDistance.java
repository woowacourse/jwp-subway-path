package subway.exception;

import org.springframework.http.HttpStatus;

public class EmptySectionsHasNotDistance extends CustomException {

    public EmptySectionsHasNotDistance() {
        super("빈 구간은 Distance가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
