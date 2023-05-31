package subway.line.domain;

import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

class UpstreamTerminalSection extends AbstractSection {

    public UpstreamTerminalSection(Station downstream) {
        super(DummyTerminalStation.getInstance(), downstream);
    }

    public UpstreamTerminalSection(UpstreamTerminalSection otherSection) {
        this(otherSection.getDownstream());
    }

    @Override
    public List<AbstractSection> insertInTheMiddle(Station stationToAdd, int distance) {
        try {
            final AbstractSection firstSection = new UpstreamTerminalSection(stationToAdd);
            final AbstractSection secondSection = new MiddleSection(stationToAdd, getDownstream(), distance);

            return new ArrayList<>(List.of(firstSection, secondSection));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UpstreamTerminalSection::insertInTheMiddle", e);
        }
    }

    @Override
    public AbstractSection merge(AbstractSection sectionToMerge) {
        validateNotTerminalSection(sectionToMerge);
        validateSectionLinked(sectionToMerge);

        return new UpstreamTerminalSection(sectionToMerge.getDownstream());
    }
}
