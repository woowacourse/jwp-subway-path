package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.vo.Section;

import java.util.List;

public abstract class Reader {
    protected final SectionDao sectionDao;

    public Reader(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    abstract public List<Section> save(final CaseDto caseDto) throws IllegalAccessException;

    abstract public List<Section> initializeSave(final CaseDto caseDto, final List<Section> allSection) throws IllegalAccessException;
}
