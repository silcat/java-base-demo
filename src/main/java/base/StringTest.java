package base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

/**
 * String不可变
 */
public class StringTest {
    public static void main(String[] args)  {
        try {
            throw new Exception("");
        }catch (RuntimeException r){

        }catch (Throwable ex){


        }        bukebian(1L);
    }

    /**
     * String,Integer等不可变性
     * 不可变主要指指针地址不会改变，为保证指针所指内容也不变value通过一个Arrays.copyOf的方式拷贝一个数组再给到对象的成员变量
     * @param num
     */
    private static void bukebian( Long num){
        int sInteger = 22;
        Integer integer = new Integer(sInteger);
        sInteger = 33;
        System.out.println(sInteger);
        System.out.println(integer);

        student sStudent = new student("小红", null);
        student student = new student("小明",sStudent);
        sStudent.name ="小白";
        System.out.println(student.getStudent().name);
        sStudent = new student("小黑", null);
        System.out.println(student.getStudent().name);
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class student{
        private String name;
        private student student;
    }
}
