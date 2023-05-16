package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.repository.SectionRepository;

@Component
public class InsertDownwardStation implements InsertStrategyInterface {

    private final SectionRepository sectionRepository;

    public InsertDownwardStation(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Sections sections, InsertSection insertSection) {
        return sections.isDownwardStation(insertSection.getDownStation());
    }

    @Override
    public Long insert(Sections sections, InsertSection insertSection) {
        final Distance requestDistance = insertSection.getDistance();
        final Section targetSection = findTargetSection(sections, insertSection);

        validateDistance(targetSection.getDistance(), requestDistance);

        final Section updateSection = createUpdateSection(insertSection, targetSection);
        final Section newSection = createNewSection(insertSection, targetSection);

        sectionRepository.update(updateSection);
        return sectionRepository.insert(newSection);
    }

    private Section createNewSection(InsertSection insertSection, Section targetSection) {
        return new Section(
                targetSection.getDistance().minus(insertSection.getDistance()),
                targetSection.getUpStation(),
                insertSection.getUpStation(),
                targetSection.getLineId()
        );
    }

    private Section createUpdateSection(InsertSection insertSection, Section targetSection) {
        return new Section(
                insertSection.getDistance(),
                insertSection.getUpStation(),
                insertSection.getDownStation(),
                targetSection.getLineId()
        );
    }

    private Section findTargetSection(Sections sections, InsertSection insertSection) {
        return sections.findDownwardStationSection(insertSection.getDownStation());
    }
}
