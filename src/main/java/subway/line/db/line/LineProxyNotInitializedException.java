package subway.line.db.line;

import subway.common.exception.BusinessException;

public class LineProxyNotInitializedException extends BusinessException {

    public LineProxyNotInitializedException() {
        super("지하철 노선 프록시 객체가 초기화되지 않았습니다. 저장된 객체를 업데이트 하는지 확인해주세요");
    }
}
