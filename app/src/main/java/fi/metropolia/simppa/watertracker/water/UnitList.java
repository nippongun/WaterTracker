package fi.metropolia.simppa.watertracker.water;

import java.util.ArrayList;

class UnitList {
    private static final UnitList ourInstance = new UnitList();

    static UnitList getInstance() {
        return ourInstance;
    }

    private ArrayList<Unit> unitList;

    private UnitList() {
        unitList = new ArrayList<Unit>();
    }

    public int getUnitListVolume(){
        int i = 0;
        for (Unit unit : unitList){
            i += unit.getVolume();
        }
        return i;
    }

    public void add(Unit unit){
        unitList.add(unit);
    }
}
