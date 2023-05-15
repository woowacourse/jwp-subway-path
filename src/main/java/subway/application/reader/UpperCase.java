package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.Section;

import java.util.List;

public class UpperCase extends Reader{
    public UpperCase(SectionDao sectionDao) {
        super(sectionDao);
    }

    @Override
    public List<Section> read(CaseDto caseDto) throws IllegalAccessException {
        if(caseDto.getCaseType().equals(CaseType.UPPER)){
            final Section deleteSection = caseDto.getDeleteSection();
            sectionDao.deleteSection(deleteSection.getId());

            final long lineId = caseDto.getLineId();
            final String departure = caseDto.getDeparture();
            final String middle = caseDto.getArrival();
            final String arrival = deleteSection.getArrival().getName();
            final int firstSectionDistance = caseDto.getDistance();
            final int secondSectionDistance = deleteSection.getDistance().getDistance() - firstSectionDistance;

            final long upSectionId = sectionDao.saveSection(lineId,firstSectionDistance,departure,middle);
            final long downSectionId = sectionDao.saveSection(lineId,secondSectionDistance,middle,arrival);

            return List.of(new Section(upSectionId, departure, middle, firstSectionDistance),
                    new Section(downSectionId, middle, arrival, secondSectionDistance));
        }
        return new LowerCase(sectionDao).read(caseDto);
    }
}
