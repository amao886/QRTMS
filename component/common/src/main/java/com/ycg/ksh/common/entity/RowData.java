package com.ycg.ksh.common.entity;

import java.util.List;

public class RowData {

    private int rowIndex;

    private List<CellData> cells;

    public RowData(){}

    public RowData(int index){
        this.rowIndex = index;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<CellData> getCells() {
        return cells;
    }

    public void setCells(List<CellData> cells) {
        this.cells = cells;
    }
}
