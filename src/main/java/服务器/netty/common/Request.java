package 服务器.netty.common;

import lombok.*;

/**
 * 统一API响应结果封装
 */
@Builder
@Data
@NoArgsConstructor
public class Request {


    /**
     * 返回消息
     */
    private String msg ="";


    public Request( String msg) {
        this.msg = msg;
    }


}
