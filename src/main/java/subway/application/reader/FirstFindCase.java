package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.Section;

import java.util.List;

public class FirstFindCase extends Reader {
    public FirstFindCase(SectionDao sectionDao) {
        super(sectionDao);
    }

    @Override
    public List<Section> read(final CaseDto caseDto) throws IllegalAccessException {
        if(caseDto.getCaseType().equals(CaseType.NON_DELETE_SAVE_CASE)){
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
        return new UpperCase(sectionDao).read(caseDto);
    }
}
