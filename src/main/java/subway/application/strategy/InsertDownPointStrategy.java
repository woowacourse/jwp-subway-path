package subway.application.strategy;

import org.springframework.stereotype.Component;
import subway.domain.Section;
import subway.domain.Sections;
import subway.repository.SectionRepository;

@Component
public class InsertDownPointStrategy extends InsertStrategy {

    public InsertDownPointStrategy(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    boolean support(Sections sections, InsertSection insertSection) {
        return sections.isDownStationPoint(insertSection.getDownStation());
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
    protected Section findTargetSection(Sections sections, InsertSection insertSection) {
        return sections.getTargtDownStationSection(insertSection.getDownStation());
    }
}
