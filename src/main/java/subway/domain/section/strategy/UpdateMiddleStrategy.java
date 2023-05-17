package subway.domain.section.strategy;

import java.util.List;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.exception.StationNotFoundInSectionsException;

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
            sections.addAll(originIndex, originSection.splitByPrev(section));
        }
        if (isEqualNext(section, originSection)) {
            sections.addAll(originIndex, originSection.splitByNext(section));
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
                .orElseThrow(StationNotFoundInSectionsException::new);
    }

    @Override
    public List<Section> removeStation(final List<Section> sections, final Station station) {
        final Section prevSection = findPrevSection(sections, station);
        final Section nextSection = findNextSection(sections, station);
        final int index = sections.indexOf(prevSection);

        sections.remove(prevSection);
        sections.remove(nextSection);
        sections.add(index, prevSection.concatSection(nextSection));
        return sections;
    }

    private static Section findPrevSection(final List<Section> newSections, final Station station) {
        return newSections.stream()
                .filter(section -> section.isEqualNextStation(station))
                .findAny()
                .orElseThrow(StationNotFoundInSectionsException::new);
    }

    private static Section findNextSection(final List<Section> newSections, final Station station) {
        return newSections.stream()
                .filter(section -> section.isEqualPrevStation(station))
                .findAny()
                .orElseThrow(StationNotFoundInSectionsException::new);
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
