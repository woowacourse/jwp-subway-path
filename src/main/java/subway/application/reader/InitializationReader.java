package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.vo.Section;

import java.util.List;

public class InitializationReader extends Reader {
    public InitializationReader(SectionDao sectionDao) {
        super(sectionDao);
    }

    @Override
    public List<Section> save(CaseDto caseDto) throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public List<Section> initializeSave(CaseDto caseDto, List<Section> allSection) {
        return new NonDeleteSaveCase(sectionDao).save(CaseTypeSetter.setCase(caseDto, allSection));
    }
}
