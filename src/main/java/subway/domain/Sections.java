package subway.domain;

import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public WeightedMultigraph<Station, Section> setEdgeWeightToGraph(WeightedMultigraph<Station, Section> graph) {
        for (Section section : sections) {
            graph = section.addWeightedEdges(graph);
        }
        return graph;
    }

    public void addNewSection(Section newSection) {
        sections.add(newSection);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
