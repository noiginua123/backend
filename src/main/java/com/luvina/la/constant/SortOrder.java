/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * SortOrder.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.constant;

/**
 * Liệt kê các hướng sắp xếp được ADM002 hỗ trợ.
 *
 * @author thanhvinh
 */
public enum SortOrder {

    ASC("ASC"),

    DESC("DESC");

    public static final String ASC_VALUE = "ASC";

    public static final String DESC_VALUE = "DESC";

    private final String value;

    /**
     * Khởi tạo hướng sắp xếp.
     *
     * @param value Giá trị truyền xuống native query
     */
    SortOrder(String value) {
        this.value = value;
    }

    /**
     * Lấy giá trị chuỗi của hướng sắp xếp.
     *
     * @return Giá trị ASC hoặc DESC
     */
    public String getValue() {
        return value;
    }

    /**
     * Kiểm tra chuỗi có phải hướng sắp xếp hợp lệ hay không.
     *
     * @param value Giá trị cần kiểm tra
     * @return true nếu giá trị là ASC hoặc DESC
     */
    public static boolean isSupported(String value) {
        return ASC_VALUE.equals(value) || DESC_VALUE.equals(value);
    }

    /**
     * Chuyển chuỗi thành hướng sắp xếp, mặc định ASC khi rỗng.
     *
     * @param value Giá trị hướng sắp xếp
     * @return Enum hướng sắp xếp tương ứng
     */
    public static SortOrder fromValueOrDefault(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ASC;
        }
        return DESC_VALUE.equals(value.trim()) ? DESC : ASC;
    }
}
