package designMode.syncPromise;

import java.util.concurrent.Future;

/**
 * 凭证：封装future，一个凭证状态只能修改一次
 * 凭证有3种状态：成功，失败与取消
 */
public interface IPromise<V> extends IFuture<V>  {



}
