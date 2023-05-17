package subway.Integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.SectionCreateRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.LinesResponse;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.entity.StationEntity;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

public class LineControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    private Line lineTwo;
    private Station upward;
    private Station downward;

    @BeforeEach
    void setUp() {
        lineTwo = lineRepository.save(new Line("2호선", "초록색", 500));
        upward = stationRepository.save(new Station("잠실역"));
        downward = stationRepository.save(new Station("잠실새내역"));
        lineTwo.addSection(upward, downward, 10);
        lineRepository.update(lineTwo);
    }

    @Test
    @DisplayName("노선 목록을 조회한다.")
    void findLines() throws Exception {
        final Line lineFour = lineRepository.save(new Line("4호선", "하늘색", 1000));
        final Station lineFourUpward = stationRepository.save(new Station("이수역"));
        final Station lineFourDownward = stationRepository.save(new Station("서울역"));
        lineFour.addSection(lineFourUpward, lineFourDownward, 10);
        lineRepository.update(lineFour);

        final MvcResult mvcResult = mockMvc.perform(get("/lines"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final LinesResponse response =
                new LinesResponse(List.of(LineResponse.from(lineTwo), LineResponse.from(lineFour)));
        final String jsonResponse = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        final LinesResponse result = objectMapper.readValue(jsonResponse, LinesResponse.class);
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Nested
    @DisplayName("노선 생성 요청시 ")
    class CreateLine {

        @Test
        @DisplayName("유효한 노선 정보라면 새로운 노선을 추가한다.")
        void createLine() throws Exception {
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색", 500);

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
            final LineCreateRequest request = new LineCreateRequest(" ", "초록색", 500);

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
            final LineCreateRequest request = new LineCreateRequest("2호선", " ", 500);

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("노선 색깔은 공백일 수 없습니다."));
        }

        @Test
        @DisplayName("역 간의 거리가 입력되지 않으면 400 상태를 반환한다.")
        void createSectionWithoutDistance() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(1L, 2L, null);

            mockMvc.perform(post("/lines/{id}/sections", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("역 간의 거리는 존재해야 합니다."));
        }

        @Test
        @DisplayName("역 간의 거리가 0이하이면 400 상태를 반환한다.")
        void createSectionWithNegativeDistance() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(1L, 2L, -1);

            mockMvc.perform(post("/lines/{id}/sections", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("역 간의 거리는 0보다 커야합니다."));
        }
    }

    @Nested
    @DisplayName("노선 조회 시 ")
    class FindLine {

        @Test
        @DisplayName("존재하는 노선이라면 노선 정보를 조회한다.")
        void findLine() throws Exception {
            final MvcResult mvcResult = mockMvc.perform(get("/lines/{id}", lineTwo.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            final LineResponse response = LineResponse.from(lineTwo);
            final String jsonResponse = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
            final LineResponse result = objectMapper.readValue(jsonResponse, LineResponse.class);
            assertThat(result).usingRecursiveComparison().isEqualTo(response);
        }

        @Test
        @DisplayName("ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void findLineWithInvalidIDType() throws Exception {
            mockMvc.perform(get("/lines/{id}", "l"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
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
}
