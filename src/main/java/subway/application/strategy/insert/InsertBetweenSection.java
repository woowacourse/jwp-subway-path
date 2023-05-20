package subway.application.strategy.insert;

import subway.domain.Distance;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

public abstract class InsertBetweenSection implements InsertStationStrategy {

    protected final SectionRepository sectionRepository;

    protected InsertBetweenSection(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public final Long insert(SingleLineSections sections, InsertSection insertSection) {
        final Distance requestDistance = insertSection.getDistance();
        final Section targetSection = findTargetSection(sections, insertSection);

        validateDistance(targetSection.getDistance(), requestDistance);

        final Section updateSection = createUpdateSection(insertSection, targetSection);
        final Section newSection = createNewSection(insertSection, targetSection);

        return sectionRepository.insertAndUpdate(newSection, updateSection);
    }

    protected abstract Section findTargetSection(SingleLineSections sections, InsertSection insertSection);

    protected abstract Section createUpdateSection(InsertSection insertSection, Section targetSection);

    protected abstract Section createNewSection(InsertSection insertSection, Section targetSection);

    private void validateDistance(Distance targetDistance, Distance distance) {
        if (targetDistance.isShorterThanAndEqual(distance)) {
            throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
        }
    }
}
