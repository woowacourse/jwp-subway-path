package subway.repository.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.entity.StationEntity;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class StationConverterTest {

    @Test
    @DisplayName("StationConverter 를 이용해 StationEntity 를 Station 으로 변경한다.")
    void entityToDomain() {
        StationEntity stationEntity = new StationEntity(1L, "existsId");

        Station station = StationConverter.entityToDomain(stationEntity);

        assertThat(station.getId()).isEqualTo(1);
        assertThat(station.getName()).isEqualTo("existsId");
    }

    @Test
    @DisplayName("StationConverter 를 이용해 StationEntity 를 Station 으로 변경 (ID 는 따로 주입)")
    void entityToDomainWithId() {
        StationEntity stationEntity = new StationEntity("noExistsId");

        Station station = StationConverter.entityToDomain(1L, stationEntity);

        assertThat(station.getId()).isEqualTo(1L);
        assertThat(station.getName()).isEqualTo("noExistsId");
    }

    @Test
    @DisplayName("StationConverter 를 이용해 Station 를 StationEntity 으로 변경한다. (ID 있음)")
    void domainToEntityWithId() {
        Station station = new Station(1L, "existsId");

        StationEntity stationEntity = StationConverter.domainToEntity(station);

        assertThat(stationEntity.getId()).isEqualTo(1);
        assertThat(stationEntity.getName()).isEqualTo("existsId");
    }

    @Test
    @DisplayName("StationConverter 를 이용해 Station 을 StationEntity 로 변경한다. (ID 없음)")
    void domainToEntity() {
        Station station = new Station("existsId");

        StationEntity stationEntity = StationConverter.domainToEntity(station);

        assertThat(stationEntity.getId()).isZero();
        assertThat(stationEntity.getName()).isEqualTo("existsId");
    }

}
