package subway.application.reader;

import subway.domain.vo.Section;

public class CaseDto {
    private final Long lineId;
    private final String departure;
    private final String arrival;
    private final int distance;
    private final CaseType caseType;
    private final Section deleteSection;

    private CaseDto(Builder builder) {
        this.lineId = builder.lineId;
        this.departure = builder.departure;
        this.arrival = builder.arrival;
        this.distance = builder.distance;
        this.caseType = builder.caseType;
        this.deleteSection = builder.deleteSection;
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

    public static class Builder {
        private Long lineId;
        private String departure;
        private String arrival;
        private int distance;
        private CaseType caseType;
        private Section deleteSection;

        public Builder lineId(final long id) {
            this.lineId = id;
            return this;
        }

        public Builder departure(final String departure) {
            this.departure = departure;
            return this;
        }

        public Builder arrival(final String arrival) {
            this.arrival = arrival;
            return this;
        }

        public Builder distance(final int distance) {
            this.distance = distance;
            return this;
        }

        public Builder caseType(final CaseType caseType) {
            this.caseType = caseType;
            return this;
        }

        public Builder deleteSection(final Section deleteSection) {
            this.deleteSection = deleteSection;
            return this;
        }

        public CaseDto build() {
            return new CaseDto(this);
        }
    }
}

