package subway.domain.section.strategy;

import static subway.domain.section.SectionFactory.createRemoveCentralCase;

import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

public class UpdateMiddleStrategy implements UpdateSectionsStrategy {

    @Override
    public List<Section> addSection(final List<Section> sections, final Section section) {
        return sections;
    }

    @Override
    public List<Section> removeStation(final List<Section> sections, final Station station) {
        final Section beforeSection = findBeforeSection(station, sections);
        final Section nextSection = findNextSection(station, sections);

        final int index = sections.indexOf(beforeSection);
        sections.remove(beforeSection);
        sections.remove(nextSection);
        sections.add(index, createRemoveCentralCase(beforeSection, nextSection));
        return sections;
    }

    private static Section findBeforeSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.isEqualNextStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    private static Section findNextSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.isEqualPrevStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }
}
