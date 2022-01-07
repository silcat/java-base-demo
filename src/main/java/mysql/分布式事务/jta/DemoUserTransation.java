package mysql.分布式事务.jta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.*;

/**
 * 面向开发人员的接口为 UserTransaction ，开发人员通常只使用此接口实现 JTA 事务管理，
 */
@Component
public class DemoUserTransation implements UserTransaction {

    @Autowired
    private TransactionManager demoTransationManager;
    @Override
    public void begin() throws NotSupportedException, SystemException {
        demoTransationManager.begin();
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {

    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {

    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {

    }

    @Override
    public int getStatus() throws SystemException {
        return 0;
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {

    }
}
