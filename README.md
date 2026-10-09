# Bài kiểm tra cuối module – Phát triển ứng dụng Web với JSP & Servlet

**Product Management:** Ứng dụng quản lý sản phẩm xây dựng bằng Java 17, Servlet, JSP, JDBC, MySQL và Maven. Dự án có cấu trúc MVC, dữ liệu mẫu và hướng dẫn kiểm thử.

## Chức năng

| Yêu cầu | Đã triển khai |
|---|---|
| Danh sách sản phẩm | Bảng ID, tên, giá, số lượng từ MySQL |
| Thêm sản phẩm | Form nhập thông tin và lưu dữ liệu |
| Sửa sản phẩm | Tải dữ liệu hiện có vào form để chỉnh sửa |
| Xóa sản phẩm | Trang xác nhận trước khi xóa |
| Cơ sở dữ liệu | JDBC / PreparedStatement; bảng `products` |

## Môi trường

- Java Development Kit **17+**
- Maven **3.8+**
- MySQL **8+**
- Tomcat **10.1+** (cần Jakarta Servlet 6, không dùng Tomcat 9)

## Cài đặt và chạy

**1. Tạo database:** Mở MySQL Workbench rồi thực thi toàn bộ file `database.sql`. File sẽ tạo `product_management_db`, bảng `products` và ba sản phẩm mẫu, không xóa sản phẩm cũ.

**2. Cấu hình kết nối:** Trong `JdbcProductDAO`, ứng dụng đọc biến môi trường khi Tomcat khởi động:

| Biến môi trường | Mặc định |
|---|---|
| `PRODUCT_DB_URL` | `jdbc:mysql://localhost:3306/product_management_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh` |
| `PRODUCT_DB_USER` | `root` |
| `PRODUCT_DB_PASSWORD` | rỗng |

Không lưu mật khẩu thực tế trong mã nguồn. Khi MySQL có mật khẩu, cấu hình các biến này trong môi trường chạy Tomcat.

**3. Build WAR:** Mở terminal tại thư mục chứa `pom.xml` rồi chạy:

```bash
mvn clean package
```

Maven tạo `target/product-management.war`. Chép file WAR vào thư mục `webapps/` của Tomcat 10.1+ và khởi động Tomcat.

**4. Mở ứng dụng:**

```text
http://localhost:8080/product-management/products
```

## Cấu trúc

```text
pom.xml
database.sql
README.md
TESTING.md
src/main/java/com/codegym/
  model/Product.java
  dao/ProductDAO.java
  dao/JdbcProductDAO.java
  web/ProductServlet.java
src/main/webapp/
  index.jsp
  assets/style.css
  WEB-INF/web.xml
  WEB-INF/views/list.jsp
  WEB-INF/views/form.jsp
  WEB-INF/views/delete.jsp
  WEB-INF/views/error.jsp
```

## Kiểm thử

Thực hiện thêm, sửa, xóa sản phẩm và kiểm tra bản ghi bằng:

```sql
SELECT id, name, price, quantity
FROM product_management_db.products
ORDER BY id DESC;
```

Thử nhập tên trống, giá âm, giá nhiều hơn hai chữ số thập phân, hoặc số lượng âm để kiểm tra validation. Các kịch bản cụ thể xem trong [TESTING.md](TESTING.md).

## Ghi chú kỹ thuật

- Dùng `BigDecimal` cho giá tiền và `PreparedStatement` cho các thao tác CRUD.
- Đối với JSP, dữ liệu được hiển thị qua JSTL `c:out` hoặc `fn:escapeXml`.
- Thay đổi dữ liệu chỉ được thực hiện qua POST, có token CSRF phiên làm việc.
- Khi lưu thành công, chuyển hướng về danh sách theo Post/Redirect/Get.

**Phạm vi:** Đây là bài thực hành; chưa có hệ thống tài khoản và phân quyền, không sử dụng cho hệ thống công khai.

## Nộp bài

Theo yêu cầu CodeGym, nộp ZIP mã nguồn tên `<MSSV>_FinalExam.zip`, bao gồm cả `database.sql` và `pom.xml`. Thay `<MSSV>` bằng mã sinh viên thật. Không cần nộp thư mục `target/`.
