package subway.application.sectionreader;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;

public abstract class SectionReader {
    protected final AddStationRequest addStationRequest;
    protected final SectionDao sectionDao;

    public SectionReader(AddStationRequest addStationRequest, SectionDao sectionDao) {
        this.addStationRequest = addStationRequest;
        this.sectionDao = sectionDao;
    }

    abstract public List<AddStationResponse> read(long id, List<Section> sections) throws IllegalAccessException;

    abstract public List<AddStationResponse> addSection(long id, List<Section> departureSections, List<Section> arrivalSections) throws IllegalAccessException;

    abstract public List<AddStationResponse> addSectionWithDirection(long id, List<Section> departureSections, List<Section> arrivalSections, Section section) throws IllegalAccessException;
}
