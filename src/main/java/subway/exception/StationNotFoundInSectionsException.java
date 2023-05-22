package subway.exception;

import org.springframework.http.HttpStatus;

public class StationNotFoundInSectionsException extends CustomException {

    public StationNotFoundInSectionsException() {
        super("삭제하려는 역은 해당 노선에 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
