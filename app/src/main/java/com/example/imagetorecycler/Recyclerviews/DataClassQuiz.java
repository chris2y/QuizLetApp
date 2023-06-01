package com.example.imagetorecycler.Recyclerviews;


import java.util.ArrayList;
import java.util.List;

public class DataClassQuiz {
    private String dataDepartment, dataYear,ItemId, dataExamType, dataUniversity, dataCourse,dataDate;
    private ArrayList<String> dataImage;
    private String key;


    public DataClassQuiz(String dataDepartment, String dataYear, String itemId, String dataExamType,
                         ArrayList<String> dataImage, String dataUniversity, String dataCourse, String dataDate) {
        this.dataDepartment = dataDepartment;
        this.dataYear = dataYear;
        this.ItemId = itemId;
        this.dataExamType = dataExamType;
        this.dataImage = dataImage;
        this.dataUniversity = dataUniversity;
        this.dataCourse = dataCourse;
        this.dataDate = dataDate;
    }

    public DataClassQuiz() {

    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDataExamType() {
        return dataExamType;
    }
    public void setDataExamType(String dataExamType) {
        this.dataExamType = dataExamType;
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

    public String getDataUniversity() {
        return dataUniversity;
    }

    public void setDataUniversity(String dataUniversity) {
        this.dataUniversity = dataUniversity;
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
}