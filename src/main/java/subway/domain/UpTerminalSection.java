package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class UpTerminalSection extends AbstractSection {

    public UpTerminalSection(Station downstream) {
        super(DummyTerminalStation.getInstance(), downstream);
    }

    public UpTerminalSection(UpTerminalSection otherSection) {
        this(otherSection.getDownstream());
    }

    @Override
    public List<AbstractSection> insertInTheMiddle(Station stationToAdd, int distance) {
        try {
            final AbstractSection firstSection = new UpTerminalSection(stationToAdd);
            final AbstractSection secondSection = new MiddleSection(stationToAdd, getDownstream(), distance);

            return new ArrayList<>(List.of(firstSection, secondSection));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UpTerminalSection::insertInTheMiddle", e);
        }
    }

    @Override
    public AbstractSection merge(AbstractSection sectionToMerge) {
        validateNotTerminalSection(sectionToMerge);
        validateSectionLinked(sectionToMerge);

        return new UpTerminalSection(sectionToMerge.getDownstream());
    }
}
