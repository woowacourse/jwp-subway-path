package subway.persistence.dao;

import org.junit.jupiter.api.Test;
import subway.persistence.entity.SectionEntity;

import static org.junit.jupiter.api.Assertions.*;

class SectionDaoTest {

    @Test
    void adf() {
        final SectionEntity sectionEntity = SectionEntity.Builder.builder()
                .id(1L)
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(3)
                .build();

        System.out.println("sectionEntity = " + sectionEntity);
    }

}
