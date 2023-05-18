package subway.exception;

import org.springframework.http.HttpStatus;

public class SplitSectionIsSmallerThanSplitterException extends CustomException {

    public SplitSectionIsSmallerThanSplitterException() {
        super("요청한 구간의 길이가 분리하려는 구간의 길이보다 더 크거나 같습니다.", HttpStatus.BAD_REQUEST);
    }
}
