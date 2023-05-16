package subway.domain.section.strategy;

import static subway.domain.section.SectionFactory.createAddConnectedNextCase;
import static subway.domain.section.SectionFactory.createAddConnectedPrevCase;
import static subway.domain.section.SectionFactory.createRemoveMiddleCase;

import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

public class UpdateMiddleStrategy implements UpdateSectionsStrategy {

    private static final UpdateMiddleStrategy INSTANCE = new UpdateMiddleStrategy();
    private static final UpdateTailStrategy UPDATE_TAIL_STRATEGY = UpdateTailStrategy.getInstance();
    private static final UpdateHeadStrategy UPDATE_HEAD_STRATEGY = UpdateHeadStrategy.getInstance();

    private UpdateMiddleStrategy() {
    }

    public static UpdateMiddleStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Section> addSection(final List<Section> sections, final Section section) {
        final Section originSection = findOriginSection(section, sections);

        final int originIndex = sections.indexOf(originSection);
        if (isEqualPrev(section, originSection)) {
            sections.add(originIndex, createAddConnectedPrevCase(section, originSection));
            sections.add(originIndex, section);
        }
        if (isEqualNext(section, originSection)) {
            sections.add(originIndex, section);
            sections.add(originIndex, createAddConnectedNextCase(section, originSection));
        }
        sections.remove(originSection);
        return sections;
    }

    private boolean isEqualPrev(final Section section, final Section originSection) {
        return originSection.isEqualPrevStation(section.getPrevStation());
    }

    private boolean isEqualNext(final Section section, final Section originSection) {
        return originSection.isEqualNextStation(section.getNextStation());
    }

    private static Section findOriginSection(final Section section, final List<Section> sections) {
        return sections.stream()
                .filter(element -> element.getPrevStation().equals(section.getPrevStation())
                        || element.getNextStation().equals(section.getNextStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이전 역을 찾을 수 없습니다."));
    }

    @Override
    public List<Section> removeStation(final List<Section> sections, final Station station) {
        final Section beforeSection = findBeforeSection(sections, station);
        final Section nextSection = findNextSection(sections, station);
        final int index = sections.indexOf(beforeSection);

        sections.remove(beforeSection);
        sections.remove(nextSection);
        sections.add(index, createRemoveMiddleCase(beforeSection, nextSection));
        return sections;
    }

    private static Section findBeforeSection(final List<Section> newSections, final Station station) {
        return newSections.stream()
                .filter(section -> section.isEqualNextStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    private static Section findNextSection(final List<Section> newSections, final Station station) {
        return newSections.stream()
                .filter(section -> section.isEqualPrevStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    @Override
    public boolean supportAddSection(final List<Section> sections, final Section section) {
        return !UPDATE_HEAD_STRATEGY.supportAddSection(sections, section)
                && !UPDATE_TAIL_STRATEGY.supportAddSection(sections, section);
    }

    @Override
    public boolean supportRemoveStation(final List<Section> sections, final Station station) {
        return !UPDATE_HEAD_STRATEGY.supportRemoveStation(sections, station)
                && !UPDATE_TAIL_STRATEGY.supportRemoveStation(sections, station);
    }
}
