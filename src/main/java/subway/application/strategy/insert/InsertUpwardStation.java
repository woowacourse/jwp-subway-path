package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.Section;
import subway.domain.Sections;
import subway.repository.SectionRepository;

@Component
public class InsertUpwardStation extends InsertBetweenSection {

    protected InsertUpwardStation(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    public boolean support(Sections sections, InsertSection insertSection) {
        return sections.isUpwardStation(insertSection.getUpStation());
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
    protected Section findTargetSection(Sections sections, InsertSection insertSection) {
        return sections.findUpwardStationSection(insertSection.getUpStation());
    }
}
