package subway.application.port.out.line;

import subway.domain.Line;
import subway.domain.LineInfo;

public interface PersistLinePort {

    long create(LineInfo lineInfo);

    void updateInfo(long lineId, LineInfo lineInfo);

    // TODO: 파라미터 (lineId, Sections)로 수정
    void updateSections(Line line);

    void deleteById(long lineId);
}
