package designMode.actionObject;


import lombok.Data;

/**
 * 中介者模式：用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。
 * 多个对象之间互相引用的关系可以使用中介者
 * 具体同事类之间的交互复杂性集中到了中介者类中，中介者成了最复杂的类
 */
public class MediatorPattern {
    public static void main(String[] args)  {
        MsgMediator msgMediator = new MsgMediator();
        AbstractColleague colleague = new Colleague(msgMediator);
        AbstractColleague colleagueTwo = new ColleagueTwo(msgMediator);
        msgMediator.setColleague(colleague);
        msgMediator.setColleagueTwo(colleagueTwo);
        colleague.doSomething("colleagueTwo hello");
        colleague.doSomething("colleague hello");
    }

    //抽象同事类
    public static abstract class  AbstractColleague {
        public AbstractMediator mediator;

        public AbstractColleague() {
        }

        public AbstractColleague(AbstractMediator mediator) {
            this.mediator = mediator;
        }

        public abstract void doSomething(String msg);
        public abstract void acceptMsg(String msg);

    }
    //具体同事类
    public static  class  Colleague extends AbstractColleague {

        public Colleague(AbstractMediator mediator) {
            super(mediator);
        }
        @Override
        public void doSomething(String msg) {
            System.out.println("Colleague dosomething  "+msg);
            super.mediator.doExtraSomething(this,msg);
        }

        @Override
        public void acceptMsg(String msg) {
            System.out.println("Colleague acceptMsg "+ msg);
        }
    }
    public static  class  ColleagueTwo extends AbstractColleague {

        public ColleagueTwo(AbstractMediator mediator) {
            super(mediator);
        }
        @Override
        public void doSomething(String msg) {
            System.out.println("ColleagueTwo doSomething"+msg);
            super.mediator.doExtraSomething(this,msg);
        }

        @Override
        public void acceptMsg(String msg) {
            System.out.println("ColleagueTwo acceptMsg "+msg);
        }
    }

    //中介者(同事可以用map或quene存储)
    @Data
    public static abstract class  AbstractMediator {
        public AbstractColleague colleague;
        public AbstractColleague colleagueTwo;

        public abstract void doExtraSomething(AbstractColleague colleague,String msg);
    }
    //发送消息具体中介者
    public static class  MsgMediator extends AbstractMediator{

        @Override
        public void doExtraSomething(AbstractColleague colleague, String msg) {
            if (colleague instanceof Colleague ){
                super.colleagueTwo.acceptMsg(msg);
            }else if (colleague instanceof ColleagueTwo ){
                super.colleague.acceptMsg(msg);
            }
        }


    }


}