package com.cbq.yatori.core.aggregate;

/**
 * Yatori统一活动器
 * @param <T>
 */
public interface YatoriAction<T,R> {
    public void preOperation(T t,R r); //前置操作，用于活动的前置信息处理
    public void action(T t,R r); //活动操作，活动的核心执行
    public void postOperation(T t,R r); //活动后置操作，就是活动执行过后的修整操作
}
