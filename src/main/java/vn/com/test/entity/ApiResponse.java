package vn.com.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Successfully", data);
    }

    public static <T> ApiResponse<T> error(int code,String message) {
        return new ApiResponse<>(code, message, null);
    }
}
