package com.example.imagetorecycler.Recyclerviews;


import java.util.ArrayList;
import java.util.List;

public class DataClassAssignment {
    private String dataDepartment, dataYear,ItemId, dataUserID, dataDescription, dataCourse,dataDate;
    private ArrayList<String> dataImage;
    private String key;


    public DataClassAssignment(String dataDepartment, String dataYear, String itemId,
                               ArrayList<String> dataImage, String dataDescription, String dataCourse, String dataDate,String dataUserID) {
        this.dataDepartment = dataDepartment;
        this.dataYear = dataYear;
        this.ItemId = itemId;
        this.dataImage = dataImage;
        this.dataDescription = dataDescription;
        this.dataCourse = dataCourse;
        this.dataDate = dataDate;
        this.dataUserID = dataUserID;
    }

    public DataClassAssignment() {

    }


    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getDataDepartment() {
        return dataDepartment;
    }
    public void setDataDepartment(String dataDepartment) {
        this.dataDepartment = dataDepartment;
    }

    public String getDataYear() {
        return dataYear;
    }
    public void setDataYear(String dataYear) {
        this.dataYear = dataYear;
    }


    public String getItemId() {
        return ItemId;
    }
    public void setItemId(String itemId) {
        ItemId = itemId;
    }


    public List<String> getDataImage() {
        return dataImage;
    }
    public void setDataImage(ArrayList<String> dataImage) {
        this.dataImage = dataImage;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    public void setDataDescription(String dataDescription) {
        this.dataDescription = dataDescription;
    }

    public String getDataCourse() {
        return dataCourse;
    }

    public void setDataCourse(String dataCourse) {
        this.dataCourse = dataCourse;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getDataUserID() {
        return dataUserID;
    }

    public void setDataUserID(String dataUserID) {
        this.dataUserID = dataUserID;
    }
}