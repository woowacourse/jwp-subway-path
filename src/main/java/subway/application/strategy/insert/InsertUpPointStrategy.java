package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.Section;
import subway.domain.Sections;
import subway.repository.SectionRepository;

@Component
public class InsertUpPointStrategy extends InsertStrategy {

    public InsertUpPointStrategy(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    boolean support(Sections sections, InsertSection insertSection) {
        return sections.isUpStationPoint(insertSection.getUpStation());
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
        return sections.getTargtUpStationSection(insertSection.getUpStation());
    }
}
