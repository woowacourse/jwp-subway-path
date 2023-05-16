package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class SectionChange {

    private List<Section> newSections;
    private List<Section> updatedSections;
    private List<Section> deletedSections;

    public SectionChange() {
        newSections = new ArrayList<>();
        updatedSections = new ArrayList<>();
        deletedSections = new ArrayList<>();
    }

    public void addNewSection(Section section) {
        newSections.add(section);
    }

    public void addUpdatedSection(Section section) {
        updatedSections.add(section);
    }

    public void addDeletedSection(Section section) {
        deletedSections.add(section);
    }

    public List<Section> getNewSections() {
        return newSections;
    }

    public List<Section> getUpdatedSections() {
        return updatedSections;
    }

    public List<Section> getDeletedSections() {
        return deletedSections;
    }
}
