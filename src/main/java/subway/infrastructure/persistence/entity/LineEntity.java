package subway.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;

public class LineEntity {

    private Long id;
    private String name;
    private List<SectionEntity> sectionEntities = new ArrayList<>();

    public LineEntity(final Long id, final String name, final List<SectionEntity> sectionEntities) {
        this.id = id;
        this.name = name;
        this.sectionEntities = sectionEntities;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SectionEntity> getSectionEntities() {
        return sectionEntities;
    }
}
