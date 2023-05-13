package subway.Integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import subway.controller.dto.request.StationCreateRequest;
import subway.domain.station.Station;
import subway.repository.StationRepository;

class StationControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Nested
    @DisplayName("역 추가 요청시 ")
    class CreateStation {

        @Test
        @DisplayName("유효한 역 정보라면 새로운 역을 추가한다")
        void createStation() throws Exception {
            final StationCreateRequest request = new StationCreateRequest("잠실역");

            mockMvc.perform(post("/stations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, containsString("/stations/")));
        }

        @Test
        @DisplayName("역 이름이 잘못되면 400 상태를 반환한다.")
        void createStationWithInvalidName() throws Exception {
            final StationCreateRequest request = new StationCreateRequest(" ");

            mockMvc.perform(post("/stations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("역 이름은 공백일 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("역 정보 조회 시 ")
    class FindStation {

        private Station station;

        @BeforeEach
        void setUp() {
            station = stationRepository.save(new Station("잠실역"));
        }

        @Test
        @DisplayName("유효한 ID라면 역 정보를 조회한다.")
        void findStation() throws Exception {
            mockMvc.perform(get("/stations/{id}", station.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(station.getId()))
                    .andExpect(jsonPath("$.name").value(station.getName()));
        }

        @Test
        @DisplayName("ID가 유효하지 않다면 400 상태를 반환한다.")
        void findStationWithInvalidID() throws Exception {
            mockMvc.perform(get("/stations/{id}", "poi"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
