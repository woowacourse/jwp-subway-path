package subway.exception;

public class SectionInvalidException extends RuntimeException {

    public SectionInvalidException() {
        super("잘못된 Section이 들어왔습니다. 역의 정보를 다시 확인해주세요.");
    }
}
