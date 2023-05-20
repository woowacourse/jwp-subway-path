package subway.controller.section;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.SectionController;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.exception.InvalidDistanceException;
import subway.exception.SectionDuplicatedException;
import subway.exception.SectionForkedException;
import subway.exception.SectionNotConnectException;
import subway.exception.SectionNotFoundException;
import subway.exception.UpStationNotFoundException;
import subway.service.SectionService;
import subway.service.SubwayMapService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SectionController.class)
public class SectionControllerUnitTest {

    @MockBean
    private SectionService sectionService;

    @MockBean
    private SubwayMapService subwayMapService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("구간을 생성한다.")
    void create_section_success() throws Exception {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);

        // when & then
        mockMvc.perform(
                        post("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionCreateRequest)))
                .andExpect(status().isCreated());

        verify(sectionService).insertSection(any(SectionCreateRequest.class));
    }

    @Test
    @DisplayName("해당하는 노선을 찾을 수 없다면 예외를 발생시킨다.")
    void throws_exception_when_not_found_section() throws Exception {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        doAnswer(invocation -> {
            throw new SectionNotFoundException();
        }).when(sectionService).insertSection(any(SectionCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionCreateRequest)))
                .andExpect(status().isNotFound());

        verify(sectionService).insertSection(any(SectionCreateRequest.class));
    }

    @Test
    @DisplayName("상행 종점을 찾을 수 없다면 예외를 발생시킨다.")
    void throws_exception_when_not_found_up_station_section() throws Exception {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        doAnswer(invocation -> {
            throw new UpStationNotFoundException();
        }).when(sectionService).insertSection(any(SectionCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionCreateRequest)))
                .andExpect(status().isNotFound());

        verify(sectionService).insertSection(any(SectionCreateRequest.class));
    }

    @Test
    @DisplayName("역 사이의 거리가 1보다 작으면 예외를 발생시킨다.")
    void throws_exception_when_invalid_distance_between_section() throws Exception {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        doAnswer(invocation -> {
            throw new InvalidDistanceException();
        }).when(sectionService).insertSection(any(SectionCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(sectionService).insertSection(any(SectionCreateRequest.class));
    }

    @Test
    @DisplayName("노선이 일치하지 않아 해당하는 구간을 연결할 수 없는 경우 예외를 발생시킨다.")
    void throws_exception_when_not_connect_section() throws Exception {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        doAnswer(invocation -> {
            throw new SectionNotConnectException();
        }).when(sectionService).insertSection(any(SectionCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(sectionService).insertSection(any(SectionCreateRequest.class));
    }

    @Test
    @DisplayName("중복되는 구간이면 예외를 발생시킨다.")
    void throws_exception_when_duplicated_section() throws Exception {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        doAnswer(invocation -> {
            throw new SectionDuplicatedException();
        }).when(sectionService).insertSection(any(SectionCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(sectionService).insertSection(any(SectionCreateRequest.class));
    }

    @Test
    @DisplayName("갈래길이 생기는 노선이면 예외를 발생시킨다.")
    void throws_exception_when_forked_section() throws Exception {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        doAnswer(invocation -> {
            throw new SectionForkedException();
        }).when(sectionService).insertSection(any(SectionCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(sectionService).insertSection(any(SectionCreateRequest.class));
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void delete_section_success() throws Exception {
        // given
        SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest(2, "잠실역");

        // when
        mockMvc.perform(
                        delete("/sections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sectionDeleteRequest)))
                .andExpect(status().isNoContent());

        // then
        verify(sectionService).deleteSection(any(SectionDeleteRequest.class));
    }
}
