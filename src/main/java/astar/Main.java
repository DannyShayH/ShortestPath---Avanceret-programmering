package astar;

import java.sql.Array;
import java.util.*;

public class Main {
    static int[][] grid = {
            {0,0,0,1,0,0,0,1,0,0,0,0},
            {1,1,0,1,0,1,0,1,0,1,1,0},
            {0,0,0,0,0,1,0,0,0,0,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,0,0,0,0,0,0,0,1,0,0,0},
            {1,1,1,0,1,1,1,0,1,1,0,0},
            {0,0,0,0,0,0,1,0,0,0,0,0},
            {0,1,1,1,1,1,0,1,1,1,1,0},
            {0,0,0,0,1,0,0,0,0,0,1,0},
            {1,1,0,1,1,0,1,1,0,1,1,0},
            {0,0,0,0,0,0,1,0,0,0,1,0},
            {0,1,1,1,0,1,1,0,1,1,1,0},
    };

    static final int ROWS = 12, COLUMNS = 12;

    static String[][] CITY_NAMES = new String[ROWS][COLUMNS];

    static {
        CITY_NAMES[0][0] = "Helsinge";
        CITY_NAMES[0][2] = "Duemose";
        CITY_NAMES[3][0] = "Gribsø";
        CITY_NAMES[4][3] = "Slotspavillionen";
        CITY_NAMES[4][7] = "Hillerød";
        CITY_NAMES[6][9] = "Favrholm";
        CITY_NAMES[7][11] = "Birkerød";
        CITY_NAMES[11][11] = "Nørreport";
    }

    static void main() {
        CityNode[][] nodes = new CityNode[ROWS][COLUMNS];
        for(int row = 0; row < ROWS; row++){
            for(int columns = 0; columns < COLUMNS; columns++){
                if(grid[row][columns] == 0){
                    nodes[row][columns] = new CityNode(row, columns);
                }
            }
        }

        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for(int row = 0; row < ROWS; row++){
            for(int columns = 0; columns < COLUMNS; columns++){
                if(nodes[row][columns] == null ) continue;
                for(int[] direction : directions){
                    int newRow = row + direction[0], newColumn = columns + direction[1];
                    if(newRow >= 0 && newRow < ROWS
                            && newColumn >= 0
                            && newColumn < COLUMNS
                            && nodes[newRow][newColumn] != null){
                        nodes[row][columns].addCities(nodes[newRow][newColumn]);
                    }
                }
            }
        }

        CityNode source = nodes[0][0];
        CityNode destination = nodes[11][11];

        findShortestPath(source, destination);
    }

    private static int heuristic(CityNode node, CityNode destination){
        return Math.abs(destination.getRow() - node.getRow())
                + Math.abs(destination.getColumn() - node.getColumn());
    }

    public static class NodeWithDist implements Comparable<NodeWithDist> {
        CityNode node;
        int gCost;
        int fCost;

        public NodeWithDist(CityNode node, int gCost, int hCost){
            this.node = node;
            this.gCost = gCost;
            this.fCost = gCost + hCost;
        }

        @Override
        public int compareTo(NodeWithDist other) {return Integer.compare(this.fCost, other.fCost);}
    }

    public static void findShortestPath(CityNode source, CityNode destination){
        Map<CityNode, CityNode> prev = new HashMap<>();
        Map<CityNode, Integer> distance = new HashMap<>();
        Set<CityNode> visited = new HashSet<>();

        PriorityQueue<NodeWithDist> queue = new PriorityQueue<>();
        queue.add(new NodeWithDist(source, 0, heuristic(source, destination)));
        distance.put(source, 0);

        while(!queue.isEmpty()) {
            NodeWithDist current = queue.poll();

            if(current.node.equals(destination)) break;
            if(visited.contains(current.node)) continue;
            visited.add(current.node);

            for(CityNode next : current.node.getCities()){
                if(visited.contains(next)) continue;

                int newDist = current.gCost + 1;

                if(newDist < distance.getOrDefault(next, Integer.MAX_VALUE)){
                    distance.put(next, newDist);
                    prev.put(next, current.node);
                    queue.add(new NodeWithDist(next, newDist, heuristic(next, destination)));
                }
            }
        }

        if(!distance.containsKey(destination)){
            System.out.println("\nIngen vej fundet fra " + source
                    + " til " + destination);
            return;
        }

        List<String> path = new ArrayList<>();
        CityNode step = destination;
        while(step != null){
            String cityName = CITY_NAMES[step.getRow()][step.getColumn()];

            if(cityName != null){
                path.add(cityName + "(" + step.getRow() + "," + step.getColumn() + ")");
            }else {
                path.add("(" + step.getRow() + "," + step.getColumn() + ")");
            }
            step = prev.get(step);
        }

        Collections.reverse(path);

        System.out.println("\nKorteste vej: " + String.join(" -> ", path));
        System.out.println("Samlet distance: " + distance.get(destination));
    }
}
