package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.domain.LineProperty;
import subway.application.repository.LinePropertyRepository;
import subway.application.service.command.in.IdCommand;
import subway.application.service.command.in.SaveLinePropertyCommand;
import subway.application.service.command.in.UpdateLinePropertyCommand;
import subway.presentation.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LinePropertyService {

    private final LinePropertyRepository linePropertyRepository;

    public LinePropertyService(LinePropertyRepository linePropertyRepository) {
        this.linePropertyRepository = linePropertyRepository;
    }

    public LineResponse saveLineProperty(SaveLinePropertyCommand command) {
        LineProperty lineProperty = linePropertyRepository.insert(command.toEntity());
        return LineResponse.of(lineProperty);
    }

    public List<LineResponse> findLinePropertyResponses() {
        List<LineProperty> allLineProperties = findLineProperties();
        return allLineProperties.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<LineProperty> findLineProperties() {
        return linePropertyRepository.findAll();
    }

    public LineResponse findLinePropertyResponseById(IdCommand command) {
        LineProperty lineProperty = linePropertyRepository.findById(command.getId());
        return LineResponse.of(lineProperty);
    }

    public void updateLineProperty(UpdateLinePropertyCommand command) {
        linePropertyRepository.update(command.toEntity());
    }

    public void deleteLinePropertyById(IdCommand command) {
        linePropertyRepository.deleteById(command.getId());
    }
}
