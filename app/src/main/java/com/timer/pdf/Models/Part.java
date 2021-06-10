package com.timer.pdf.Models;

public class Part {
    String name, number, count;

    public Part(String name, String number, String count) {
        this.name = name;
        this.number = number;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
