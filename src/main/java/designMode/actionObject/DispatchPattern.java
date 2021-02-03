package designMode.actionObject;


import java.util.Random;

/**
 * 双分派模式：
 */
public class DispatchPattern {
    public static void main(String[] args)  {


    }


    /**
     * 它定义了一个接受访问者（accept）的方法，其意义是指每一个元素都要可以被访问者访问。
     */
    // 员工基类
    public abstract class Staff {

        public String name;
        public int kpi;// 员工KPI

        public Staff(String name) {
            this.name = name;
            kpi = new Random().nextInt(10);
        }
        // 核心方法，接受Visitor的访问
        public abstract void accept(Visitor visitor);
    }
    // 工程师
    public class Engineer extends Staff {

        public Engineer(String name) {
            super(name);
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
        // 工程师一年的代码数量
        public int getCodeLines() {
            return new Random().nextInt(10 * 10000);
        }
    }
    // 经理
    public class Manager extends Staff {

        public Manager(String name) {
            super(name);
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
        // 一年做的产品数量
        public int getProducts() {
            return new Random().nextInt(10);
        }
    }

    /**
     * Visitor：定义了对每个访问的行为
     */
    public interface Visitor {

        // 访问工程师类型
        void visit(Engineer engineer);

        // 访问经理类型
        void visit(Manager manager);
    }
    // CEO访问者
    public class CEOVisitor implements Visitor {
        @Override
        public void visit(Engineer engineer) {
            System.out.println("工程师: " + engineer.name + ", KPI: " + engineer.kpi);
        }

        @Override
        public void visit(Manager manager) {
            System.out.println("经理: " + manager.name + ", KPI: " + manager.kpi +
                    ", 新产品数量: " + manager.getProducts());
        }
    }
    // CTO访问者
    public class CTOVisitor implements Visitor {
        @Override
        public void visit(Engineer engineer) {
            System.out.println("工程师: " + engineer.name + ", KPI: " + engineer.kpi);
        }

        @Override
        public void visit(Manager manager) {
            System.out.println("经理: " + manager.name + ", KPI: " + manager.kpi +
                    ", 新产品数量: " + manager.getProducts());
        }
    }
}