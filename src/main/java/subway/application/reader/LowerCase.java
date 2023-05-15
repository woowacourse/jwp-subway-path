package subway.application.reader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.AddStationResponse;

import java.util.List;

public class LowerCase extends Reader{
    public LowerCase(SectionDao sectionDao) {
        super(sectionDao);
    }

    @Override
    public List<Section> read(CaseDto caseDto) throws IllegalAccessException {
        if(caseDto.getCaseType().equals(CaseType.LOWER)){
            final Section deleteSection = caseDto.getDeleteSection();
            sectionDao.deleteSection(deleteSection.getId());

            final long lineId = caseDto.getLineId();
            final String departure = deleteSection.getDeparture().getName();
            final String middle = caseDto.getArrival();
            final String arrival = caseDto.getArrival();
            final int secondSectionDistance = caseDto.getDistance();
            final int firstSectionDistance = deleteSection.getDistance().getDistance() - secondSectionDistance;

            final long upSectionId = sectionDao.saveSection(lineId,firstSectionDistance,departure,middle);
            final long downSectionId = sectionDao.saveSection(lineId,secondSectionDistance,middle,arrival);

            return List.of(new Section(upSectionId, departure, middle, firstSectionDistance),
                    new Section(downSectionId, middle, arrival, secondSectionDistance));
        }
        return new ExceptionCase(sectionDao).read(caseDto);
    }
}
