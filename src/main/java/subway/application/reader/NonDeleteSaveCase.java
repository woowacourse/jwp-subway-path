package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.vo.Section;

import java.util.List;

public class NonDeleteSaveCase extends Reader {
    public NonDeleteSaveCase(SectionDao sectionDao) {
        super(sectionDao);
    }

    @Override
    public List<Section> save(final CaseDto caseDto) {
        if (caseDto.getCaseType().equals(CaseType.NON_DELETE_SAVE_CASE)) {
            final long sectionId = sectionDao.saveSection(
                    caseDto.getLineId(),
                    caseDto.getDistance(),
                    caseDto.getDeparture(),
                    caseDto.getArrival()
            );
            return List.of(new Section(
                    sectionId,
                    caseDto.getDeparture(),
                    caseDto.getArrival(),
                    caseDto.getDistance()
            ));
        }
        return new UpperCase(sectionDao).save(caseDto);
    }

    @Override
    public List<Section> initializeSave(CaseDto caseDto, List<Section> allSection) throws IllegalAccessException {
        throw new IllegalAccessException("이미 type을 선언했습니다.");
    }
}
