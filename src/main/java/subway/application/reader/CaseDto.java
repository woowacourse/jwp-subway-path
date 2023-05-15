package subway.application.reader;

import subway.domain.Section;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CaseDto {
    private final Long lineId;
    private final String departure;
    private final String arrival;
    private final int distance;
    private CaseType caseType;
    private Section deleteSection;
    public CaseDto(Long lineId, String departure, String arrival, int distance) {
        this.lineId = lineId;
        this.departure = departure;
        this.arrival = arrival;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public int getDistance() {
        return distance;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public Section getDeleteSection() {
        return deleteSection;
    }

    public void setCase(final List<Section> allSection){
        if(allSection.isEmpty()){
            caseType = CaseType.NON_DELETE_SAVE_CASE;
            return;
        }
        List<Section> departureMatch = allSection.stream()
                .filter(section -> section.getDeparture().getName().equals(this.departure))
                .collect(toList());
        List<Section> crossMatch = allSection.stream()
                .filter(section -> section.getDeparture().getName().equals(this.arrival)||
                        section.getArrival().getName().equals(this.departure))
                .collect(toList());
        List<Section> arrivalMatch = allSection.stream()
                .filter(section -> section.getArrival().getName().equals(this.arrival))
                .collect(toList());
        this.caseType = findCase(departureMatch,crossMatch,arrivalMatch);
    }
    public CaseType findCase(final List<Section> departureMatch,
                         final List<Section> crossMatch,
                         final List<Section> arrivalMatch){
        if(crossMatch.size()>1||(!departureMatch.isEmpty()&!arrivalMatch.isEmpty())){
            return CaseType.EXCEPTION_CASE;
        }
        if(crossMatch.size()==1 & departureMatch.isEmpty() & arrivalMatch.isEmpty()){
            return CaseType.NON_DELETE_SAVE_CASE;
        }
        if(!departureMatch.isEmpty()&arrivalMatch.isEmpty()){
            deleteSection = departureMatch.get(0);
            return CaseType.UPPER;
        }
        if(departureMatch.isEmpty()&!arrivalMatch.isEmpty()){
            deleteSection = arrivalMatch.get(0);
            return CaseType.LOWER;
        }
        return CaseType.EXCEPTION_CASE;
    }
}

