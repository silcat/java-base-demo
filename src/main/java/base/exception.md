#分类
* Exception
    * 受检查的异常，这种异常是强制我们catch或throw的异常。你遇到这种异常必须进行catch或throw，如果不处理，编译器会报错。比如：IOException
    * 继承自throw
* RuntimeException
    * 运行时异常，这种异常我们不需要处理，完全由虚拟机接管。比如我们常见的NullPointerException，我们在写程序时不会进行catch或throw
    * 继承自Exception
* Error
    * 严重的问题发生了，而且这种错误是不可恢复的
    * Error是一种严重的问题，可以被try-catach，但应用程序不应该捕捉它。
    * 继承自throw
