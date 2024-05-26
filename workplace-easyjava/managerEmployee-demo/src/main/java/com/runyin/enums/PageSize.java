package com.runyin.enums;

public enum PageSize {
    SIZE10(10),
    SIZE15(15),
    SIZE20(20),
    SIZE30(30),
    SIZE40(40),
    SIZE50(50),
    SIZE60(60);
    private int size;
    PageSize(int size){
        this.size =size;
    }

    public int getSize(){
        return this.size;
    }

}
