package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.ObjectType;

import java.io.Serializable;

public class PossibleObject extends BaseEntity {

    private ObjectType noteType;
    private String objectKey;
    private String oppositeKey;

    private String shipperKey;//发货方
    private String receiveKey;//收货方
    private String conveyKey;//承运商

    public PossibleObject() {
    }

    public PossibleObject(ObjectType noteType, Serializable objectKey, Serializable _shipperKey, Serializable _receiveKey, Serializable _conveyKey) {
        this.noteType = noteType;
        this.objectKey = String.valueOf(objectKey);
        shipperKey = String.valueOf(_shipperKey);
        receiveKey = String.valueOf(_receiveKey);
        conveyKey = String.valueOf(_conveyKey);
    }

    public PossibleObject(ObjectType noteType, Serializable objectKey, Serializable oppositeKey, Serializable _shipperKey, Serializable _receiveKey, Serializable _conveyKey) {
        this.noteType = noteType;
        this.objectKey = String.valueOf(objectKey);
        this.oppositeKey = String.valueOf(oppositeKey);
        shipperKey = String.valueOf(_shipperKey);
        receiveKey = String.valueOf(_receiveKey);
        conveyKey = String.valueOf(_conveyKey);
    }


    public ObjectType getNoteType() {
        return noteType;
    }

    public void setNoteType(ObjectType noteType) {
        this.noteType = noteType;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getShipperKey() {
        return shipperKey;
    }

    public void setShipperKey(String shipperKey) {
        this.shipperKey = shipperKey;
    }

    public String getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(String receiveKey) {
        this.receiveKey = receiveKey;
    }

    public String getConveyKey() {
        return conveyKey;
    }

    public void setConveyKey(String conveyKey) {
        this.conveyKey = conveyKey;
    }

    public String getOppositeKey() {
        return oppositeKey;
    }

    public void setOppositeKey(String oppositeKey) {
        this.oppositeKey = oppositeKey;
    }
}

