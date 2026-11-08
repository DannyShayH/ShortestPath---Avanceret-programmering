package astar;

import java.util.ArrayList;
import java.util.List;


public class CityNode {
    private int row;
    private int column;
    private String name;
    private List<CityNode> cities;

    public CityNode(int row, int column){
        this.row = row;
        this.column = column;
        this.cities = new ArrayList<>();
    }

    public int getRow(){return row;}
    public int getColumn(){return column;}
    public List<CityNode> getCities(){return cities;}

    public void addCities(CityNode cityNode){cities.add(cityNode);}
}
