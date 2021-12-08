package spring.aop;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class Aop {

    private String name;

    public Aop() {

    }

    @TestAop
    public String test() {
       return "test";
    }


}

