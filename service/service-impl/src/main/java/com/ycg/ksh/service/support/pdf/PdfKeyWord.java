package com.ycg.ksh.service.support.pdf;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/19
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * PDF关键字
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/19
 */
public class PdfKeyWord extends BaseEntity {

    private String word;

    private String image;

    private List<PdfCoordinate> coordinates;

    public PdfKeyWord() {
    }

    public PdfKeyWord(String word, String image) {
        this.word = word;
        this.image = image;
    }

    public void add(PdfCoordinate coordinate){
        if(coordinates == null){
            coordinates = new ArrayList<PdfCoordinate>();
        }
        coordinates.add(coordinate);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PdfCoordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<PdfCoordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
