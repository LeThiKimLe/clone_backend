package com.example.QuanLyNhaXe.enumration;

public enum Action {
    CHANGE("Đổi"),
    CANCEL("Hủy");

    private String label;

    private Action(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}