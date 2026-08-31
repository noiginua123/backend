/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * SortField.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.constant;

/**
 * Liệt kê các cột có thể sắp xếp trên ADM002.
 *
 * @author thanhvinh
 */
public enum SortField {

    EMPLOYEE_NAME("employeeName"),

    CERTIFICATION_NAME("certificationName"),

    END_DATE("endDate");

    public static final String EMPLOYEE_NAME_VALUE = "employeeName";

    public static final String CERTIFICATION_NAME_VALUE = "certificationName";

    public static final String END_DATE_VALUE = "endDate";

    private final String value;

    /**
     * Khởi tạo cột sắp xếp.
     *
     * @param value Giá trị truyền xuống native query
     */
    SortField(String value) {
        this.value = value;
    }

    /**
     * Lấy giá trị chuỗi của cột sắp xếp.
     *
     * @return Tên cột theo contract API
     */
    public String getValue() {
        return value;
    }

    /**
     * Chuyển chuỗi thành cột sắp xếp, mặc định theo tên nhân viên.
     *
     * @param value Tên cột sắp xếp
     * @return Enum cột sắp xếp tương ứng
     */
    public static SortField fromValueOrDefault(String value) {
        if (value != null) {
            String trimmedValue = value.trim();
            if (CERTIFICATION_NAME_VALUE.equals(trimmedValue)) {
                return CERTIFICATION_NAME;
            }
            if (END_DATE_VALUE.equals(trimmedValue)) {
                return END_DATE;
            }
        }
        return EMPLOYEE_NAME;
    }
}
