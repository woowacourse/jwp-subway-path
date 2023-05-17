package subway.exception;

import org.springframework.http.HttpStatus;

public class EmptySectionsCanNotRemoveSection extends CustomException {

    public EmptySectionsCanNotRemoveSection() {
        super("빈 구간에서는 제거를 할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
