package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.Section;

import java.util.List;

public abstract class Reader {
    protected final SectionDao sectionDao;

    public Reader(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }
    abstract public List<Section> read(final CaseDto caseDto) throws IllegalAccessException;
}
