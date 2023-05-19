package subway.exception.line;

public class NonExistLineException extends LineException {
    public NonExistLineException(final String inputName) {
        super("해당 조건을 충족하는 노선은 존재하지 않습니다. (입력값 : " + inputName + ")");
    }

    public NonExistLineException(final Long inputId) {
        super("해당 조건을 충족하는 노선은 존재하지 않습니다. (입력값 : " + inputId + ")");
    }
}
