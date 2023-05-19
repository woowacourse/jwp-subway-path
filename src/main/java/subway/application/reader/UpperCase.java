package subway.application.reader;

import subway.application.exception.AddSectionException;
import subway.dao.SectionDao;
import subway.domain.vo.Section;

import java.util.List;

public class UpperCase extends Reader {
    public UpperCase(SectionDao sectionDao) {
        super(sectionDao);
    }

    @Override
    public List<Section> save(CaseDto caseDto) {
        if (caseDto.getCaseType().equals(CaseType.UPPER)) {
            final Section deleteSection = caseDto.getDeleteSection();
            validateDistance(caseDto.getDistance(), deleteSection.getDistanceValue());
            sectionDao.deleteSection(deleteSection.getId());

            final long lineId = caseDto.getLineId();
            final String departure = caseDto.getDeparture();
            final String middle = caseDto.getArrival();
            final String arrival = deleteSection.getArrivalValue();
            final int firstSectionDistance = caseDto.getDistance();
            final int secondSectionDistance = deleteSection.getDistanceValue() - firstSectionDistance;

            final long upSectionId = sectionDao.saveSection(lineId, firstSectionDistance, departure, middle);
            final long downSectionId = sectionDao.saveSection(lineId, secondSectionDistance, middle, arrival);

            return List.of(new Section(upSectionId, departure, middle, firstSectionDistance),
                    new Section(downSectionId, middle, arrival, secondSectionDistance));
        }
        return new LowerCase(sectionDao).save(caseDto);
    }

    @Override
    public List<Section> initializeSave(CaseDto caseDto, List<Section> allSection) throws IllegalAccessException {
        throw new IllegalAccessException("이미 type을 선언했습니다.");
    }

    private void validateDistance(final int requestDistance, final int distance) {
        if (requestDistance >= distance) {
            throw new AddSectionException("입력 거리가 기존 존재하는 거리보다 작아야합니다.");
        }
    }
}
