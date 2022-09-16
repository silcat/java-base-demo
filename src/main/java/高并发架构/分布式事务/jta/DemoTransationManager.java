package 高并发架构.分布式事务.jta;

import javax.transaction.*;

/**
 * 本身并不承担实际的事务处理功能，它更多的是充当用户接口和TM实现接口之间的桥梁
 */
public class DemoTransationManager implements TransactionManager {

    @Override
    public void begin() throws NotSupportedException, SystemException {

    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {

    }

    @Override
    public int getStatus() throws SystemException {
        return 0;
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        return null;
    }

    @Override
    public void resume(Transaction tobj) throws InvalidTransactionException, IllegalStateException, SystemException {

    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {

    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {

    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {

    }

    @Override
    public Transaction suspend() throws SystemException {
        return null;
    }
}
