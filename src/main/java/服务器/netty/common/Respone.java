package 服务器.netty.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应结果封装
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Respone<T> {

    /**
     * 对外返回的对象
     */
    private T data;

    /**
     * 返回状态码
     */
    private int code = 0;

    /**
     * 返回消息
     */
    private String msg ="";


    public Respone(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
