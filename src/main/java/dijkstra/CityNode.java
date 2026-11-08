package dijkstra;

import java.util.HashMap;
import java.util.Map;

public class CityNode {
    private String name;
    private Map<CityNode, Integer> cities;

    public CityNode(String name){
        this.name = name;
        this.cities = new HashMap<>();
    }

    public String getName(){return name;}

    public Map<CityNode, Integer> getCities(){return cities;}

    public void addCities(CityNode cityNode, int weight){cities.put(cityNode, weight);}

    @Override
    public String toString(){return name;}
}
