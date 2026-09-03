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

    /**
     * Giá trị mặc định của cột sắp xếp, phục vụ {@code @RequestParam}.
     * Annotation yêu cầu hằng biên dịch nên không thể thay bằng getValue().
     */
    public static final String EMPLOYEE_NAME_VALUE = "employeeName";

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
            for (SortField sortField : values()) {
                if (sortField.value.equals(trimmedValue)) {
                    return sortField;
                }
            }
        }
        return EMPLOYEE_NAME;
    }
}
