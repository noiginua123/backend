---
name: java-coding-standard
description: Quy chuẩn lập trình Java chuẩn (Javadoc, Naming Convention, Formatting, Best Practices & Exception Handling) tại Luvina Software Company.
---

# Quy chuẩn Lập trình Java (Java Coding Standards) - Luvina Software Company

Tài liệu này tổng hợp toàn bộ quy chuẩn lập trình Java áp dụng cho dự án, bao gồm: quy định viết Javadoc, quy tắc đặt tên, định dạng khoảng trắng/dấu ngoặc, tối ưu hiệu năng và xử lý ngoại lệ.

---

## 1. Quy chuẩn Javadoc Comment

### 1.1. Quy tắc chung

- Comment đầu file bắt đầu bằng `/*` hoặc `/**`, có dòng trống ` *` giữa thông tin Copyright và tên file.
- Comment class & method bắt đầu bằng `/**`, từ dòng thứ 2 trở đi bắt đầu bằng `*`, kết thúc bằng `*/`.
- **Tác giả / Công ty mặc định:**
  - Công ty: `Luvina Software Company`
  - Người tạo / Người làm: `thanhvinh`

### 1.2. Comment đầu File (File Header)

Vị trí: Đầu file `.java`, trước `package` hoặc `import`.

**Template:**

```java
/**
 * Copyright(C) [Năm hiện tại] Luvina Software
 *
 * [Tên file.java], [dd/mm/yyyy] thanhvinh
 */
```

**Ví dụ:**

```java
/**
 * Copyright(C) 2026 Luvina Software
 *
 * SortAlgorithms.java, 08/08/2026 thanhvinh
 */
```

### 1.3. Comment đầu Class (Class Header)

Vị trí: Ngay phía trên khai báo `class` hoặc `interface`.

**Template:**

```java
/**
 * [Mô tả chi tiết mục đích và chức năng của Class]
 *
 * @author thanhvinh
 */
```

**Ví dụ:**

```java
/**
 * Lớp xử lý các thuật toán sắp xếp mảng số nguyên.
 *
 * @author thanhvinh
 */
```

### 1.4. Comment cho Method (Method Header)

Vị trí: Ngay phía trên khai báo phương thức.

**Template:**

```java
/**
 * [Mô tả chi tiết mục đích và xử lý của phương thức]
 *
 * @param [name] Mô tả ý nghĩa và cách dùng của biến trong method
 * @return Mô tả giá trị trả về nếu trong method tồn tại giá trị trả về
 * @throws [ExceptionType] Mô tả ngoại lệ được bắn ra (nếu có)
 */
```

---

## 2. Quy tắc đặt tên (Naming Conventions)

### 2.1. Tên Method

- Chữ cái đầu tiên viết thường (camelCase).
- Các từ ghép nối viết hoa chữ cái đầu: `calculateTotalAmount()`, `getUserById()`.

### 2.2. Tên Biến

- Viết theo kiểu camelCase: `userIndex`, `totalPrice`.
- Nội dung tên biến phải thể hiện rõ vai trò, chức năng của biến.
- Không trùng với các từ khóa reserve của Java (`int`, `class`, `public`,...).

### 2.3. Tên Hằng số (Constants)

- Khai báo bắt buộc với từ khóa `static final`.
- Viết hoa toàn bộ chữ cái, phân cách giữa các từ bằng dấu gạch dưới `_`.
- Ví dụ: `public static final int MAX_RETRY_COUNT = 3;`

---

## 3. Quy chuẩn Định dạng & Khoảng trắng (Formatting & Spacing)

### 3.1. Quy tắc đặt khoảng trắng (Space)

1. **Toán tử:** Thêm space vào trước và sau các toán tử:
   - Gán/Thay đổi: `=`, `+=`, `-=`, `*=`, `/=`
   - So sánh: `<`, `>`, `>=`, `<=`, `==`, `!=`
   - Logic: `&&`, `||`
   - Số học: `+`, `-`, `*`, `/`, `%`
2. **Dấu chấm phẩy `;` trong câu `for`:** Thêm space sau dấu `;` (Ví dụ: `for (int i = 0; i < n; i++)`).
3. **Dấu phẩy `,`:** Thêm space đằng sau dấu phẩy `,` (Ví dụ: `doSomething(param1, param2)`).
4. **Toán tử `++` và `--`:** **KHÔNG** đưa ký tự space vào trước/sau toán tử `++` và `--`.

### 3.2. Thứ tự khai báo Field trong Class

Khai báo theo thứ tự quyền truy cập giảm dần:

1. `public`
2. `protected`
3. `default` (package-private)
4. `private`

### 3.3. Dấu ngoặc nhọn `{}` và Câu lệnh Điều kiện

- Dấu `{` mở của class/method/block nằm tại cuối dòng lệnh đầu tiên, phía trước có 1 dấu space.
- Luôn sử dụng cặp dấu `{}` cho câu lệnh `if-else` (kể cả khi chỉ có 1 câu lệnh).
- **Format chuẩn:**

```java
if (condition) {
    statements;
} else if (condition) {
    statements;
} else {
    statements;
}
```

---

## 4. Quy chuẩn Logic & Tối ưu Hiệu năng (Best Practices)

### 4.1. So sánh Chuỗi an toàn

- Sử dụng `"Chuỗi cố định".equals(variable)` thay vì `variable.equals("Chuỗi cố định")` để tránh `NullPointerException`.

### 4.2. Xử lý Chuỗi liên tục

- Sử dụng `StringBuilder` khi thao tác hoặc cập nhật chuỗi nhiều lần thay vì cộng chuỗi `String` trực tiếp.

### 4.3. So sánh Boolean

- Không so sánh `== true` hoặc `!= true`.
- **Đúng:** `if (isOK)` hoặc `if (!isOK)`

### 4.4. Vòng lặp For mở rộng (For-each)

- Khi không cần dùng đến chỉ số `index`, ưu tiên dùng vòng lặp for mở rộng:

```java
// Đúng
for (TblUser user : listUser) {
    // Process user
}
```

### 4.5. Sử dụng chỉ số linh hoạt

- Khi set param tuần tự, dùng biến đếm `i++` thay vì hardcode số index cố định:

```java
int i = 0;
query.setInteger(i++, param0);
query.setInteger(i++, param1);
```

### 4.6. Tránh gọi hàm lặp lại

- Lưu giá trị trả về của hàm vào biến tạm thay vì gọi lại hàm nhiều lần:

```java
String indexParam = request.getParameter("index");
if (indexParam != null) {
    indexParam = indexParam.trim();
}
```

---

## 5. Xử lý Ngoại lệ (Exception Handling)

### 5.1. Bắt chính xác loại Exception

- Catch đúng Exception cụ thể thay vì Catch `Exception` chung chung.

```java
// Đúng
try {
    index = Integer.parseInt(indexParam);
} catch (NumberFormatException nfe) {
    logger.error("Dữ liệu index không hợp lệ", nfe);
    index = 0;
}
```

### 5.2. Không để trống khối Catch

- Tuyệt đối không để trống catch block. Phải log lỗi hoặc xử lý fallback thích hợp.

---

## 6. Ví dụ minh họa hoàn chỉnh (Complete Java Example)

```java
/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * UserManagementService.java, 08/08/2026 thanhvinh
 */
package com.luvina.service;

import java.util.List;

/**
 * Lớp dịch vụ quản lý thông tin người dùng trong hệ thống.
 *
 * @author thanhvinh
 */
public class UserManagementService {

    public static final int DEFAULT_USER_INDEX = 0;

    private List<String> userList;

    /**
     * Khởi tạo đối tượng UserManagementService.
     *
     * @param userList Danh sách tên người dùng ban đầu
     */
    public UserManagementService(List<String> userList) {
        this.userList = userList;
    }

    /**
     * Tìm kiếm và định dạng thông tin người dùng theo tên.
     *
     * @param targetRole Vai trò cần tìm kiếm
     * @return Chuỗi kết quả danh sách người dùng đã được nối
     */
    public String processUserData(String targetRole) {
        if ("ADMIN".equals(targetRole)) {
            StringBuilder sb = new StringBuilder();
            for (String userName : userList) {
                if (userName != null) {
                    sb.append(userName.trim()).append(", ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * Chuyển đổi chuỗi chỉ số sang số nguyên an toàn.
     *
     * @param indexStr Chuỗi đầu vào chứa chỉ số
     * @return Chỉ số dưới dạng số nguyên
     */
    public int parseIndex(String indexStr) {
        if (indexStr == null) {
            return DEFAULT_USER_INDEX;
        }

        int parsedIndex;
        try {
            parsedIndex = Integer.parseInt(indexStr.trim());
        } catch (NumberFormatException nfe) {
            // Log lỗi và gán giá trị mặc định
            parsedIndex = DEFAULT_USER_INDEX;
        }
        return parsedIndex;
    }
}
```
