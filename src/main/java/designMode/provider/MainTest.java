package designMode.provider;

import juc.Test;
import oracle.jrockit.jfr.events.Bits;
import sun.security.ec.CurveDB;

/**
 * Created by admin on 2018/2/2.
 */
public class MainTest {

    public static void main(String[] args) throws Exception {
        ProviderManager.doSomething("designMode.provider.BProvider");
    }

}
