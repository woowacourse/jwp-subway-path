package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    @DisplayName("Station 을 받아 저장한 뒤 ID 를 채워 반환한다.")
    void save() {
        Station station = new Station("hello");

        Station save = stationRepository.save(station);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo("hello");
    }



}
