# Online Course Platform - Project Summary

## 1. Đối tượng chính (Entities)

-   **User**: 3 loại role (ADMIN, INSTRUCTOR, STUDENT)
-   **Course**
-   **Category**
-   **Enrollment**

## 2. Validation

-   Sử dụng `@Valid` để kích hoạt validation trong controller.
-   Các annotation đã dùng:
    -   `@NotNull` -- kiểm tra null (dùng cho các field bắt buộc nhập).
    -   `@NotBlank` -- kiểm tra String không null, không rỗng và không
        chỉ chứa khoảng trắng.
    -   `@NotEmpty` -- kiểm tra Collection hoặc String không rỗng.
    -   `@Positive` -- kiểm tra số phải \> 0 (dùng cho ID, giá tiền, số
        lượng...).
-   Áp dụng **Validation Group** (`OnCreate`, `OnUpdate`) để linh hoạt
    trong từng ngữ cảnh.

### Kinh nghiệm rút ra:

-   `@Valid` trên `@RequestBody` sẽ bắt buộc validate input JSON.
-   `@PathVariable` và `@RequestParam` không bắt buộc validate (trừ khi
    khai báo thêm annotation).
-   Tránh lạm dụng nhiều annotation validation ở Controller → nên kết
    hợp validation ở cả DTO và logic Service.

## 3. Exception Handling

-   Sử dụng **Global Exception Handler** để quản lý lỗi tập trung
    (`@RestControllerAdvice`).
-   Custom `AppException` để kiểm soát lỗi business rõ ràng.
-   Bắt các lỗi phổ biến:
    -   `MethodArgumentNotValidException` (vi phạm validation).
    -   `ConstraintViolationException` (validate cho param/path
        variable).
    -   `AppException` (lỗi nghiệp vụ).

### Kinh nghiệm rút ra:

-   Xây dựng cấu trúc response error thống nhất
    (`timestamp, status, error, message, path`).
-   Giúp client dễ dàng debug và xử lý.

## 4. Authentication & Authorization (Stateless)

-   Hệ thống **Stateless Authentication** bằng JWT:
    -   **AccessToken** (ngắn hạn) để gọi API.
    -   **RefreshToken** (dài hạn) để cấp lại access token.
-   Không dùng session, mọi request đều cần Bearer token.
-   **Phân quyền** rõ ràng:
    -   **Controller**: chỉ định role được phép gọi API bằng
        `@PreAuthorize` hoặc cấu hình `SecurityConfig`.
    -   **Service (Business Rule)**: kiểm tra role trong các thao tác
        nghiệp vụ quan trọng.

### Kinh nghiệm rút ra:

-   Token hết hạn **không cần lưu blacklist** nếu access token ngắn hạn.
-   Refresh token có thể quản lý trong DB nếu muốn tăng bảo mật (có thể
    revoke).
-   Chỉ nên lưu thông tin cần thiết trong JWT để giảm dung lượng token.

## 5. Swagger (OpenAPI)

-   Sử dụng **Springdoc OpenAPI** để tạo giao diện test API.
-   Cấu hình Swagger UI hiển thị thông tin:
    -   Tên dự án
    -   Version
    -   Thông tin bảo mật (JWT Bearer Token)
-   Mở các API cho swagger hoạt động (swagger-ui, v3/api-docs, etc).
-   Cho phép truy cập H2 console để test DB nội bộ.

### Kinh nghiệm rút ra:

-   Cần mở security cho swagger → nếu không sẽ bị chặn bởi Spring
    Security.
-   Swagger rất hữu ích để test nhanh API thay cho Postman.

## 6. MapStruct

-   Sử dụng `@AfterMapping` để xử lý mapping cho các field không khớp
    (ví dụ: role string → enum, hoặc nested object).

### Kinh nghiệm rút ra:

-   MapStruct giúp giảm code lặp khi viết converter giữa entity và DTO.
-   `@AfterMapping` rất hữu ích khi có logic custom (không map trực
    tiếp).

## 7. Cấu hình Security

-   Bật/tắt các API cần thiết:
    -   Mở Swagger và H2 console.
    -   Các API authentication (`/api/auth/**`) cho phép anonymous.
    -   Các API khác yêu cầu xác thực.

### Kinh nghiệm rút ra:

-   Nếu quên mở swagger hoặc H2 thì không test được API.
-   Luôn tách cấu hình rõ ràng cho API public và API private.
-   Phải nhớ bật `@EnableMethodSecurity(prePostEnabled = true)` khi có sử dụng `@PreAuthorize()`.
-   Nếu Role không có tiền tố `ROLE_` thì dùng `hasAuthority`.

## 8. Update Entity với Collection

-   Khi update entity có thuộc tính là Collection (ví dụ `List`, `Set`):
    -   Nếu chỉ gán collection mới trực tiếp (`entity.setCollection(newList)`), JPA có thể không hiểu là update → dễ gây lỗi **orphan**, **constraint violation**, hoặc dữ liệu cũ không bị xoá.
    -   Nên clear collection cũ rồi add từng phần tử mới vào → để Hibernate hiểu sự thay đổi và generate SQL chính xác.
    -   Hoặc dùng cascade + orphanRemoval đúng cách để Hibernate quản lý vòng đời các phần tử.
    -   Trường hợp không update được entity, hãy kiểm tra các collection có đang trả về `immutable list` hay không. Đó là nguyên hân gây lỗi nhưng lỗi ẩn message.

### Kinh nghiệm rút ra:

-   Không nên chỉ gọi `setCollection()` mà không clear, vì dễ gây **bị mất đồng bộ dữ liệu**.
-   Cách an toàn:
    ```java
    entity.getItems().clear();
    entity.getItems().addAll(newItems);
    ```
-   Nếu quan hệ là `@OneToMany` với orphanRemoval = true → khi clear thì bản ghi cũ trong DB cũng bị xoá.
-   Luôn xem lại `CascadeType` và `orphanRemoval` để tránh lỗi xoá nhầm dữ liệu.

------------------------------------------------------------------------

# Tổng Kết - Bài học rút ra

1.  **Validation** cần tổ chức hợp lý, tránh nhồi hết ở Controller, nên
    chia sẻ trách nhiệm với DTO và Service.
2.  **Exception Handler** giúp hệ thống có response lỗi thống nhất, dễ
    debug và bảo trì.
3.  **JWT Stateless Auth** phù hợp với hệ thống microservice hoặc
    API-first, không cần quản lý session.
4.  **Phân quyền** cần đặt ở cả Controller (API level) và Service
    (business rule level) để tránh bypass logic.
5.  **Swagger** là công cụ quan trọng để test và demo API → cần config
    từ đầu.
6.  **MapStruct** giúp giảm boilerplate code khi mapping Entity ↔ DTO,
    đặc biệt khi có nhiều field không khớp.
7.  **SecurityConfig** phải cẩn thận mở đúng API (swagger, H2, auth),
    nếu không sẽ rất khó test khi dev. 