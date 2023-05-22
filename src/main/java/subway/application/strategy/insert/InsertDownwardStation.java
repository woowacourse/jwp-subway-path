package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

import java.util.Objects;

@Component
public class InsertDownwardStation extends InsertBetweenSection {

    protected InsertDownwardStation(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    public boolean support(SingleLineSections sections, InsertSection insertSection) {
        return sections.getSections().stream()
                .anyMatch(section -> Objects.equals(section.getDownStation(), insertSection.getDownStation()));
    }

    @Override
    protected Section createNewSection(InsertSection insertSection, Section targetSection) {
        return new Section(
                targetSection.getDistance().minus(insertSection.getDistance()),
                targetSection.getUpStation(),
                insertSection.getUpStation(),
                targetSection.getLineId()
        );
    }

    @Override
    protected Section createUpdateSection(InsertSection insertSection, Section targetSection) {
        return new Section(
                insertSection.getDistance(),
                insertSection.getUpStation(),
                insertSection.getDownStation(),
                targetSection.getLineId()
        );
    }

    @Override
    protected Section findTargetSection(SingleLineSections sections, InsertSection insertSection) {
        return sections.findDownwardStationSection(insertSection.getDownStation());
    }
}
