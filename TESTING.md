# Kế hoạch kiểm thử Product Management

## Kiểm tra với MySQL và Tomcat

| TT | Thao tác | Kết quả cần đạt |
|---|---|---|
| 1 | Mở `/product-management/products` | Hiển thị danh sách ID / Tên / Giá / Số lượng |
| 2 | Nhấn "Thêm sản phẩm", nhập `Tai nghe`, `199000`, `15` | Chuyển về danh sách, xuất hiện Tai nghe |
| 3 | Mở "Sửa" Tai nghe | Form điền sẵn tên, giá và số lượng |
| 4 | Sửa giá thành `250000` và số lượng thành `22` | Bảng và MySQL hiển thị giá và số lượng mới |
| 5 | Nhấn "Xóa" Tai nghe rồi "Hủy bỏ" | Sản phẩm vẫn còn |
| 6 | Nhấn "Xóa" rồi "Đồng ý xóa" | Sản phẩm biến mất khỏi danh sách và MySQL |
| 7 | Nhập tên rỗng | Không tạo sản phẩm |
| 8 | Nhập giá `-1` | Bị từ chối |
| 9 | Nhập giá `2.999` | Bị từ chối |
| 10 | Nhập số lượng `-5` | Bị từ chối |
| 11 | Tải lại trang sau khi thêm | Không thêm lặp sản phẩm |
| 12 | Mở `?action=edit&id=999999999` | 404 nếu ID không tồn tại |
| 13 | Gửi POST không có token CSRF | 403 |
| 14 | Ngắt kết nối MySQL và tải lại | Trang lỗi có thông báo cấu hình, log lỗi lưu trên Tomcat |

## Truy vấn đối chiếu

```sql
USE product_management_db;
SELECT id, name, price, quantity FROM products ORDER BY id DESC;
```

## Trạng thái kiểm tra trong môi trường tạo bài

- Có thể kiểm tra cú pháp các lớp thuần Java qua `javac --release 17`.
- Cần cài Maven, Tomcat 10.1+ và MySQL để kiểm thử tích hợp toàn bộ ứng dụng. Không được coi danh sách kịch bản trên là kết quả test đã chạy trên server.
