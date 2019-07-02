package me.wcy.music.executor;

/**
 *  Executor接口 多线程
 */
public interface IExecutor<T> {
    void execute();

    void onPrepare();

    void onExecuteSuccess(T t);

    void onExecuteFail(Exception e);
}
