package com.attors.educations.Modal;

import java.util.ArrayList;
import java.util.List;

public class Test_Save {
    String id;
    int second,pos,attep,wrong,right,skip;
    List<TestpaperModal> question;

    public Test_Save(String id, int second, int pos, int attep, int wrong, int right, int skip, List<TestpaperModal> question) {
        this.id = id;
        this.second = second;
        this.pos = pos;
        this.attep = attep;
        this.wrong = wrong;
        this.right = right;
        this.skip = skip;
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getAttep() {
        return attep;
    }

    public void setAttep(int attep) {
        this.attep = attep;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public List<TestpaperModal> getQuestion() {
        return question;
    }

    public void setQuestion(List<TestpaperModal> question) {
        this.question = question;
    }
}
