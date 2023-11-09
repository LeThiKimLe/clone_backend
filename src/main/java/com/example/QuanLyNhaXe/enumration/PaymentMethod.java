package com.example.QuanLyNhaXe.enumration;

public enum PaymentMethod {
    MOMO("Momo"),
    VNPAY("VNPay"),
    BANKING("Banking");

    private String label;

    private PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
