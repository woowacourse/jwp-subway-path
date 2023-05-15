package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.Section;

import java.util.List;

public class ExceptionCase extends Reader {
    public ExceptionCase(SectionDao sectionDao) {
        super(sectionDao);
    }

    @Override
    public List<Section> read(CaseDto caseDto) throws IllegalAccessException {
        if (caseDto.getCaseType().equals(CaseType.EXCEPTION_CASE)) {
            throw new AddSectionException("역을 추가할 수 없습니다.");
        }
        throw new IllegalAccessException();
    }
}
