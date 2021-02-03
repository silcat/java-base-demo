package designMode.actionObject;


/**
 * 命令模式：把命令的调用者与执行者分开，使双方不必关心对方是如何操作的。
 * 如果使用命令模式，就要引入调用者、接收者两个角色，原本放在一处的逻辑分散到了三个类中，设计时，必须考虑这样的代价是否值得。
 * 通常创建 命令是会 默认 接收者接收者，因为client 按逻辑仅需要知道invoker即可，不会知道接收者
 */
public class CommandPattern {
    public static void main(String[] args)  {
        Invoker invoker = new Invoker();
        Receiver receiver = new Receiver();
        invoker.setCommand(new AddCommand(receiver,"test"));
        invoker.action();
        invoker.setCommand(new CopyCommand(receiver));
        invoker.action();
        invoker.undo();

    }
    //命令
    public interface Command {
        void excute();
        void undo();
    }
    public static class  CopyCommand  implements  Command{
        private Receiver receiver;
        public CopyCommand(Receiver receiver) {
           this. receiver = receiver;
        }
        @Override
        public void excute() {
            receiver.copy();
        }

        @Override
        public void undo() {

        }
    }
    public static class AddCommand  implements  Command{
        private Receiver receiver;
        private String msg;
        public AddCommand(Receiver receiver,String msg) {
            this. receiver = receiver;
            this.msg =msg;
        }
        @Override
        public void excute() {
            receiver.add(msg);
        }

        @Override
        public void undo() {
            receiver.remove(msg);
        }
    }

    //接收者
    public static class Receiver  {
        private String word;
        public void copy() {
            word = word + word;
        }
        public void add(String msg) {
            word = word + msg;
        }
        public void remove(String msg) {
            word = word.replace(msg,"");
        }
    }

    //调用者
    public static class  Invoker {
        private Command command;
        public void setCommand(Command command) {
            this.command = command;
        }
        public void action(){
            this.command.excute();
        }
        public void undo(){
            this.command.undo();
        }
    }


}