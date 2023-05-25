package subway.line.domain;

import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

class DownstreamTerminalSection extends AbstractSection {

    public DownstreamTerminalSection(Station upstream) {
        super(upstream, DummyTerminalStation.getInstance());
    }

    public DownstreamTerminalSection(DownstreamTerminalSection otherSection) {
        this(otherSection.getUpstream());
    }

    @Override
    public List<AbstractSection> insertInTheMiddle(Station stationToAdd, int distance) {
        try {
            final AbstractSection firstSection = new MiddleSection(getUpstream(), stationToAdd, distance);
            final AbstractSection secondSection = new DownstreamTerminalSection(stationToAdd);

            return new ArrayList<>(List.of(firstSection, secondSection));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("DownstreamTerminalSection::insertInTheMiddle", e);
        }
    }

    @Override
    public AbstractSection merge(AbstractSection sectionToMerge) {
        validateNotTerminalSection(sectionToMerge);
        validateSectionLinked(sectionToMerge);

        return new DownstreamTerminalSection(sectionToMerge.getUpstream());
    }
}
