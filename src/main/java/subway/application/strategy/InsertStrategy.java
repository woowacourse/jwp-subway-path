package subway.application.strategy;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.repository.SectionRepository;

abstract class InsertStrategy {

    private final SectionRepository sectionRepository;

    public InsertStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Long insert(Sections sections, InsertSection insertSection) {
        final Distance requestDistance = insertSection.getDistance();
        final Section targetSection = findTargetSection(sections, insertSection);

        validateDistance(targetSection.getDistance(), requestDistance);

        final Section updateSection = createUpdateSection(insertSection, targetSection);
        final Section newSection = createNewSection(insertSection, targetSection);

        sectionRepository.update(updateSection);
        return sectionRepository.insert(newSection);
    }

    private void validateDistance(Distance targetDistance, Distance distance) {
        if (targetDistance.isShorterThanAndEqual(distance)) {
            throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
        }
    }

    abstract boolean support(Sections sections, InsertSection insertSection);

    protected abstract Section createNewSection(InsertSection insertSection, Section targetSection);

    protected abstract Section createUpdateSection(InsertSection insertSection, Section targetSection);

    protected abstract Section findTargetSection(Sections sections, InsertSection insertSection);
}
