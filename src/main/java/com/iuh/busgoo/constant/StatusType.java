package com.iuh.busgoo.constant;

public enum StatusType {
	HOAT_DONG(1),
    TAM_NGUNG(-1),
    CHUA_KICH_HOAT(0);

    private final int value;

    StatusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
