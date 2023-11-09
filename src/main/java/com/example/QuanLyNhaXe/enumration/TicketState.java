package com.example.QuanLyNhaXe.enumration;

public enum TicketState {
    PAID("Đã thanh toán"),
    CANCELED("Đã hủy"),
    PENDING_PAYMENT("Chờ thanh toán");

    private String label;

    private TicketState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
