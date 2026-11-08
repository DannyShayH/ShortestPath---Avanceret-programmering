package dijkstra;

import java.util.*;

public class Main {
    static void main() {

        CityNode S = new CityNode("Start");
        CityNode cityNode1 = new CityNode("Helsinge");
        CityNode cityNode2 = new CityNode("Duemose");
        CityNode cityNode3 = new CityNode("Slotspavillionen");
        CityNode cityNode4 = new CityNode("Hillerød");
        CityNode cityNode5 = new CityNode("Lillerød");
        CityNode cityNode6 = new CityNode("Holte");
        CityNode cityNode7 = new CityNode("Lyngby");
        CityNode cityNode8 = new CityNode("Nørreport");
        CityNode M = new CityNode("Mål");

        S.addCities(cityNode1, 1);
        S.addCities(cityNode2, 5);
        S.addCities(cityNode3, 2);
        cityNode1.addCities(cityNode4,3);
        cityNode2.addCities(cityNode4, 2);
        cityNode3.addCities(cityNode4,1);
        cityNode5.addCities(cityNode7,2);
        cityNode6.addCities(cityNode8,4);
        cityNode4.addCities(cityNode5, 1);
        cityNode5.addCities(cityNode6,3);
        cityNode6.addCities(cityNode7,5);
        cityNode7.addCities(cityNode8,3);
        cityNode7.addCities(M, 7);
        cityNode6.addCities(M,10);
        cityNode8.addCities(M,1);

        findShortestPath(S,M);
    }

    private static class NodeWithDist implements Comparable<NodeWithDist> {
        CityNode node;
        int dist;

        public NodeWithDist(CityNode node, int dist){
            this.node = node;
            this.dist = dist;
        }

        @Override
        public int compareTo(NodeWithDist other) {return Integer.compare(this.dist, other.dist);}
    }

    public static void findShortestPath(CityNode source, CityNode destination){

        Map<CityNode, Integer> dist = new HashMap<>();
        Map<CityNode, CityNode> prev = new HashMap<>();

        Set<CityNode> visited = new LinkedHashSet<>();

        PriorityQueue<NodeWithDist> queue = new PriorityQueue<>();

        queue.add(new NodeWithDist(source, 0));
        dist.put(source, 0);

        while(!queue.isEmpty()){
            NodeWithDist current = queue.poll();

            if(visited.contains(current.node)) continue;

            visited.add(current.node);

            System.out.println("\nBesøger: " + current.node.getName()
                    + " (afstand: " + current.dist + ")");
            System.out.println("Besøgte byer: " + visited);

            if(current.node.equals(destination)) break;

            for(Map.Entry<CityNode, Integer> entry : current.node.getCities().entrySet()){
                CityNode next = entry.getKey();
                int weight = entry.getValue();

                if(visited.contains(next)) continue;

                int newDist = current.dist + weight;

                if(newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
                    dist.put(next, newDist);
                    prev.put(next, current.node);
                    queue.add(new NodeWithDist(next, newDist));
                    System.out.println("Opdaterer " + next.getName()
                            + " til afstand " + newDist);
                }
            }
        }

        if(!dist.containsKey(destination)){
            System.out.println("\nIngen vej fundet fra " + source.getName()
                    + " til " + destination.getName());
            return;
        }

        List<String> path = new ArrayList<>();
        CityNode step = destination;
        while(step != null){
            path.add(step.getName());
            step = prev.get(step);
        }

        Collections.reverse(path);

        System.out.println("\nKorteste vej: " + String.join(" -> ", path));
        System.out.println("Samlet distance: " + dist.get(destination));

    }
}
