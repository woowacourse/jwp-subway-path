package subway.entity;

public class LineSection {

    private final Long id;
    private final Long lineId;
    private final Long sectionId;

    public LineSection(final Long id, final Long lineId, final Long sectionId) {
        this.id = id;
        this.lineId = lineId;
        this.sectionId = sectionId;
    }

    public LineSection(final Long lineId, final Long sectionId) {
        this(null, lineId, sectionId);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getSectionId() {
        return sectionId;
    }
}
