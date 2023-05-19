package subway.domain;

import subway.entity.SectionDetailEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LineSections {

    private static final int FIRST_INDEX = 0;

    private final Line line;
    private final List<LineSection> sections;

    private LineSections(final Line line, final List<LineSection> sections) {
        this.line = line;
        this.sections = sections;
    }

    public static LineSections createByEntities(final List<SectionDetailEntity> detailEntities) {
        final SectionDetailEntity entity = detailEntities.get(FIRST_INDEX);
        final Line line = new Line(entity.getLineId(), entity.getLineName(), entity.getLineColor());
        final List<LineSection> sections = detailEntities.stream()
                .map(LineSection::createByEntity)
                .collect(Collectors.toUnmodifiableList());
        return new LineSections(line, LineSectionsSortFactory.sort(sections));
    }

    public Line getLine() {
        return line;
    }

    public List<LineSection> getSections() {
        return List.copyOf(sections);
    }

}
