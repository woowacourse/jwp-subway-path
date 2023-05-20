package subway.domain;

import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Subways {

    private final WeightedMultigraph<Station, SubwayEdge> subwayStructure;

    private Subways(WeightedMultigraph<Station, SubwayEdge> subwayStructure) {
        this.subwayStructure = subwayStructure;
    }

    public static Subways from(final List<Section> sections) {
        WeightedMultigraph<Station, SubwayEdge> subwayStructure = generateSubwayStructure(sections);
        return new Subways(subwayStructure);
    }

    private static WeightedMultigraph<Station, SubwayEdge> generateSubwayStructure(List<Section> sections) {
        WeightedMultigraph<Station, SubwayEdge> subwayStructure = new WeightedMultigraph<>(SubwayEdge.class);
        for (Section section : sections) {
            Station left = section.getLeft();
            Station right = section.getRight();
            SubwayEdge subwayEdge = new SubwayEdge(section.getLine(), section.getDistance());

            subwayStructure.addVertex(left);
            subwayStructure.addVertex(right);
            subwayStructure.addEdge(left, right, subwayEdge);
        }
        return subwayStructure;
    }
}
