package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

import java.util.Objects;

@Component
public class InsertUpwardStation extends InsertBetweenSection {

    protected InsertUpwardStation(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    public boolean support(SingleLineSections sections, InsertSection insertSection) {
        return sections.getSections().stream()
                .anyMatch(section -> Objects.equals(section.getUpStation(), insertSection.getUpStation()));
    }

    @Override
    protected Section createNewSection(InsertSection insertSection, Section targetSection) {
        return new Section(
                targetSection.getDistance().minus(insertSection.getDistance()),
                insertSection.getDownStation(),
                targetSection.getDownStation(),
                targetSection.getLineId()
        );
    }

    @Override
    protected Section createUpdateSection(InsertSection insertSection, Section targetSection) {
        return new Section(
                insertSection.getDistance(),
                targetSection.getUpStation(),
                insertSection.getDownStation(),
                targetSection.getLineId()
        );
    }

    @Override
    protected Section findTargetSection(SingleLineSections sections, InsertSection insertSection) {
        return sections.findUpwardStationSection(insertSection.getUpStation());
    }
}
