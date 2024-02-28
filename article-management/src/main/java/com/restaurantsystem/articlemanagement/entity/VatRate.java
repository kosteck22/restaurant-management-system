package com.restaurantsystem.articlemanagement.entity;

public enum VatRate {
    ZERO(0),
    FIVE(5),
    EIGHT(8),
    TWENTY_THREE(23);

    private final int rate;

    VatRate(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

    public static VatRate valueOf(int rate) {
        for (VatRate v : values()) {
            if (v.getRate() == rate) {
                return v;
            }
        }
        throw new IllegalArgumentException("Invalid VAT rate: " + rate);
    }
}
