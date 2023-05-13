package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class DownTerminalSection extends AbstractSection {

    public DownTerminalSection(Station upstream) {
        super(upstream, DummyTerminalStation.getInstance());
    }

    public DownTerminalSection(DownTerminalSection otherSection) {
        this(otherSection.getUpstream());
    }

    @Override
    public List<AbstractSection> insertInTheMiddle(Station stationToAdd, int distance) {
        try {
            final AbstractSection firstSection = new MiddleSection(getUpstream(), stationToAdd, distance);
            final AbstractSection secondSection = new DownTerminalSection(stationToAdd);

            return new ArrayList<>(List.of(firstSection, secondSection));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("DownTerminalSection::insertInTheMiddle", e);
        }
    }

    @Override
    public AbstractSection merge(AbstractSection sectionToMerge) {
        validateNotTerminalSection(sectionToMerge);
        validateSectionLinked(sectionToMerge);

        return new DownTerminalSection(sectionToMerge.getUpstream());
    }
}
