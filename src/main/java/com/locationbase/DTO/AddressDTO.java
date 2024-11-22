package com.locationbase.DTO;

import java.util.List;

public class AddressDTO {
    private String roadAddress;
    private String jibunAddress;
    private String englishAddress;
    private List<AddressElement> addressElements;
    private String x; // 경도
    private String y; // 위도
    private Double distance;

    // Getters and Setters

    public static class AddressElement {
        private List<String> types;
        private String longName;
        private String shortName;
        private String code;

        // Getters and Setters
    }
}
