package designMode.actionObject;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 状态模式：允许一个对象在其内部状态改变时改变它的行为，对象看起来似乎修改了它的类。
 */
public class StatePattern {
    public static void main(String[] args)  {
        Content content = new Content();
        content.send("hello");
        content.init("init");
        content.send("hello");
        content.send("hello");
        content.finish("hello");

    }
    @Data
    @AllArgsConstructor
    public static  class Content{
        public State stateOne;
        public State stateTwo;
        public State stateThree;
        public State currentState;

        public Content() {
            this.stateOne = new StateOne(this);
            this.stateTwo = new Statetwo(this);
            this.stateThree = new StateThree(this);
            this.currentState = this.stateOne;
        }
        public void init(String msg) {
            this.currentState.init(msg);
        }

        public void send(String msg) {
            this.currentState.send(msg);
        }


        public void finish(String msg) {
            this.currentState.finish(msg);

        }
    }
    //定义状态动作
    public interface   State{
        void init(String msg);
        void send(String msg);
        void finish(String msg);
    }

    @Data
    @NoArgsConstructor
    public static  class StateOne implements State{
        public Content content;

        public StateOne(Content content) {
            this.content = content;
        }

        @Override
        public void init(String msg) {
            System.out.println("StateOne is init");
            content.setCurrentState(content.stateTwo);
        }

        @Override
        public void send(String msg) {
            System.out.println("StateOne can't send msg");
        }

        @Override
        public void finish(String msg) {
            System.out.println("state is finish");
            content.setCurrentState(content.stateThree);
        }
    }
    @Data
    @NoArgsConstructor
    public static  class Statetwo implements State{
        public Content content;

        public Statetwo(Content content) {
            this.content = content;
        }

        @Override
        public void init(String msg) {
            System.out.println("StateOne already init");
        }

        @Override
        public void send(String msg) {
            System.out.println("StateTwo send msg"+ msg);
            content.setCurrentState(content.stateThree);
        }

        @Override
        public void finish(String msg) {
            System.out.println("state is finish");
            content.setCurrentState(content.stateThree);
        }
    }
    @Data
    @NoArgsConstructor
    public static  class StateThree implements State{

        public Content content;

        public StateThree(Content content) {
            this.content = content;
        }

        @Override
        public void init(String msg) {
            System.out.println("StateThree is finish");
        }

        @Override
        public void send(String msg) {
            System.out.println("StateThree is finish");
        }

        @Override
        public void finish(String msg) {
            System.out.println("StateThree is finish");
        }
    }


}