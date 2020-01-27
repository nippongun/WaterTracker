package fi.metropolia.simppa.watertracker.water;

//public enum DrinkType {Water, Soda, Juice}

import androidx.annotation.NonNull;

public class Unit {

    private String unitName;
    private int volume;

    public Unit(String unitName, int volume){
        this.unitName = unitName;
        this.volume = volume;
    }

    public String getUnitName() {
        return unitName;
    }

    public int getVolume() {
        return volume;
    }


    public String toString() {
        return unitName + volume;
    }
}
