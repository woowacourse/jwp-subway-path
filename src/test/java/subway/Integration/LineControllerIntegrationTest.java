package subway.Integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.SectionCreateRequest;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.StationEntity;
import subway.repository.LineRepository;

public class LineControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineRepository lineRepository;

    private Line lineTwo;
    private StationEntity upward;
    private StationEntity downward;

    @BeforeEach
    void setUp() {
        final LineEntity lineEntity = new LineEntity("2호선", "초록색", 300);
        lineTwo = lineRepository.save(new Line(lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare()));
        upward = stationDao.save(new StationEntity("잠실역"));
        downward = stationDao.save(new StationEntity("잠실새내역"));
        lineTwo.addSection(generateStation(upward), generateStation(downward), 10);
        lineRepository.update(lineTwo);
    }

    @Nested
    @DisplayName("노선 생성 요청시 ")
    class CreateLine {

        @Test
        @DisplayName("유효한 노선 정보라면 새로운 노선을 추가한다.")
        void createLine() throws Exception {
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색", 300);

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, containsString("/lines/")));
        }

        @Test
        @DisplayName("이름이 공백이라면 400 상태를 반환한다.")
        void createLineWithInvalidName() throws Exception {
            final LineCreateRequest request = new LineCreateRequest(" ", "초록색", 300);

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("노선 이름은 공백일 수 없습니다."));
        }

        @Test
        @DisplayName("색이 공백이라면 400 상태를 반환한다.")
        void createLineWithInvalidColor() throws Exception {
            final LineCreateRequest request = new LineCreateRequest("2호선", " ", 300);

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("노선 색깔은 공백일 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("노선 조회 시 ")
    class FindLine {

        @Test
        @DisplayName("존재하는 노선이라면 노선 정보를 조회한다.")
        void findLine() throws Exception {
            mockMvc.perform(get("/lines/{id}", lineTwo.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(lineTwo.getId()))
                    .andExpect(jsonPath("$.name").value("2호선"))
                    .andExpect(jsonPath("$.color").value("초록색"))
                    .andExpect(jsonPath("$.stations", hasSize(2)))
                    .andExpect(jsonPath("$.stations[0].id").value(upward.getId()))
                    .andExpect(jsonPath("$.stations[0].name").value("잠실역"))
                    .andExpect(jsonPath("$.stations[1].id").value(downward.getId()))
                    .andExpect(jsonPath("$.stations[1].name").value("잠실새내역"));
        }

        @Test
        @DisplayName("ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void findLineWithInvalidIDType() throws Exception {
            mockMvc.perform(get("/lines/{id}", "l"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("노선 목록을 조회한다.")
    void findLines() throws Exception {
        final LineEntity lineEntity = new LineEntity("4호선", "하늘색", 300);
        final Line lineFour = lineRepository.save(new Line(
                lineEntity.getName(),
                lineEntity.getColor(),
                lineEntity.getExtraFare()
        ));
        final StationEntity lineFourUpward = stationDao.save(new StationEntity("이수역"));
        final StationEntity lineFourDownward = stationDao.save(new StationEntity("서울역"));
        lineFour.addSection(generateStation(lineFourUpward), generateStation(lineFourDownward), 10);
        lineRepository.update(lineFour);

        mockMvc.perform(get("/lines"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lines", hasSize(2)))
                .andExpect(jsonPath("$.lines[0].id").value(lineTwo.getId()))
                .andExpect(jsonPath("$.lines[0].name").value("2호선"))
                .andExpect(jsonPath("$.lines[0].color").value("초록색"))
                .andExpect(jsonPath("$.lines[0].stations", hasSize(2)))
                .andExpect(jsonPath("$.lines[0].stations[0].id").value(upward.getId()))
                .andExpect(jsonPath("$.lines[0].stations[0].name").value("잠실역"))
                .andExpect(jsonPath("$.lines[0].stations[1].id").value(downward.getId()))
                .andExpect(jsonPath("$.lines[0].stations[1].name").value("잠실새내역"))
                .andExpect(jsonPath("$.lines[1].id").value(lineFour.getId()))
                .andExpect(jsonPath("$.lines[1].name").value("4호선"))
                .andExpect(jsonPath("$.lines[1].color").value("하늘색"))
                .andExpect(jsonPath("$.lines[1].stations", hasSize(2)))
                .andExpect(jsonPath("$.lines[1].stations[0].id").value(lineFourUpward.getId()))
                .andExpect(jsonPath("$.lines[1].stations[0].name").value("이수역"))
                .andExpect(jsonPath("$.lines[1].stations[1].id").value(lineFourDownward.getId()))
                .andExpect(jsonPath("$.lines[1].stations[1].name").value("서울역"));
    }

    @Nested
    @DisplayName("노선에 역을 등록할 시 ")
    class CreateSection {

        @Test
        @DisplayName("유효한 정보가 입력되면 노선에 역을 등록한다.")
        void createSection() throws Exception {
            final StationEntity middle = stationDao.save(new StationEntity("종합운동장역"));
            final SectionCreateRequest request = new SectionCreateRequest(upward.getId(), middle.getId(), 5);

            mockMvc.perform(post("/lines/{id}/sections", lineTwo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, containsString("/lines/" + lineTwo.getId())));
        }

        @Test
        @DisplayName("상행 역 ID가 입력되지 않으면 400 상태를 반환한다.")
        void createSectionWithoutUpwardStationId() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(null, downward.getId(), 10);

            mockMvc.perform(post("/lines/{id}/sections", lineTwo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("상행 역 ID는 존재해야 합니다."));
        }

        @Test
        @DisplayName("하행 역 ID가 입력되지 않으면 400 상태를 반환한다.")
        void createSectionWithoutDownwardStationId() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(upward.getId(), null, 10);

            mockMvc.perform(post("/lines/{id}/sections", lineTwo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("하행 역 ID는 존재해야 합니다."));
        }

        @Test
        @DisplayName("역 간의 거리가 입력되지 않으면 400 상태를 반환한다.")
        void createSectionWithoutDistance() throws Exception {
            final StationEntity middle = stationDao.save(new StationEntity("종합운동장역"));
            final SectionCreateRequest request = new SectionCreateRequest(upward.getId(), middle.getId(), null);

            mockMvc.perform(post("/lines/{id}/sections", lineTwo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("역 간의 거리는 존재해야 합니다."));
        }

        @Test
        @DisplayName("역 간의 거리가 0이하이면 400 상태를 반환한다.")
        void createSectionWithNegativeDistance() throws Exception {
            final StationEntity middle = stationDao.save(new StationEntity("종합운동장역"));
            final SectionCreateRequest request = new SectionCreateRequest(upward.getId(), middle.getId(), -1);

            mockMvc.perform(post("/lines/{id}/sections", lineTwo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("역 간의 거리는 0보다 커야합니다."));
        }
    }

    @Nested
    @DisplayName("노선에서 역 삭제 요청 시")
    class DeleteStation {

        @Test
        @DisplayName("유효한 요청이라면 역을 삭제한다.")
        void deleteStation() throws Exception {
            mockMvc.perform(delete("/lines/{lineId}", lineTwo.getId())
                            .queryParam("stationId", String.valueOf(upward.getId())))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("역 아이디가 존재하지 않으면 400 상태를 반환한다.")
        void deleteStationWithoutStationId() throws Exception {
            mockMvc.perform(delete("/lines/{lineId}", Long.MAX_VALUE))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("역 아이디로 변환할 수 없는 타입이면 400 상태를 반환한다.")
        void deleteStationWithInvalidStationIDType() throws Exception {
            mockMvc.perform(delete("/lines/{lineId}", lineTwo.getId())
                            .queryParam("stationId", "s"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    private Station generateStation(final StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
