package com.ascend.master.curator;

public interface RunningListener {

    /**
     * 启动时回调做点事情
     */
    public void processStart(Object context);

    /**
     * 关闭时回调做点事情
     */
    public void processStop(Object context);

    /**
     * 触发现在轮到自己做为active，需要载入上一个active的上下文数据
     */
    public void processActiveEnter(Object context);

    /**
     * 触发一下当前active模式失败
     */
    public void processActiveExit(Object context);

}
