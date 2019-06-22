package com.ycg.ksh.service.support.pdf;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PDF印章位置
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */
public class PdfSealPosition {

    private Collection<SealObject> sealObjects;
    private Map<SealObject, Collection<PdfCoordinate>> coordinates;
    private Collection<PdfCoordinate> pageSeals;

    public PdfSealPosition(Collection<SealObject> sealObjects) {
        this.sealObjects = sealObjects;
    }

    public PdfSealPosition(SealObject..._sealObjects) {
        if(_sealObjects != null){
            for (SealObject sealObject : _sealObjects) {
                addSealObject(sealObject);
            }
        }
    }

    public void addSealObject(SealObject sealObject){
        if(sealObjects == null){
            sealObjects = new ArrayList<SealObject>();
        }
        sealObjects.add(sealObject);
    }

    public void addCoordinate(SealObject sealObject, PdfCoordinate coordinate){
        if(coordinates == null){
            coordinates = new HashMap<SealObject, Collection<PdfCoordinate>>();
        }
        Collection<PdfCoordinate> collection = coordinates.getOrDefault(sealObject, new ArrayList<PdfCoordinate>());
        if(collection != null){
            collection.add(coordinate);
        }
        coordinates.put(sealObject, collection);
    }

    public void addPageSeal(PdfCoordinate coordinate){
        if(pageSeals == null){
            pageSeals = new ArrayList<PdfCoordinate>();
        }
        pageSeals.add(coordinate);
    }


    public boolean isEmpty(){
        if((coordinates != null && !coordinates.isEmpty()) || (pageSeals != null && !pageSeals.isEmpty())){
            return false;
        }
        return true;
    }

    public Collection<SealObject> getSealObjects() {
        return sealObjects;
    }

    public Map<SealObject, Collection<PdfCoordinate>> getCoordinates() {
        return coordinates;
    }

    public Collection<PdfCoordinate> getPageSeals() {
        return pageSeals;
    }
}
