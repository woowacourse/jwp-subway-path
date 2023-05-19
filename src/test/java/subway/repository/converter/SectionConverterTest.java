package subway.repository.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.service.domain.Distance;
import subway.service.domain.Section;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class SectionConverterTest {

    @Test
    @DisplayName("SectionConverter 를 이용해 Section 를 SectionEntity 으로 변경한다.")
    void domainToEntity() {
        Section section = new Section(
                new Station(1, "previous"),
                new Station(2, "next"),
                Distance.from(10));

        SectionEntity sectionEntity = SectionConverter.domainToEntity(1L, section);

        assertThat(sectionEntity.getLineId()).isEqualTo(1L);
        assertThat(sectionEntity.getPreviousStationId()).isEqualTo(1);
        assertThat(sectionEntity.getNextStationId()).isEqualTo(2);
        assertThat(sectionEntity.getDistance()).isEqualTo(10);
    }

}
