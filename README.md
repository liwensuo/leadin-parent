## leanin 项目实现功能和特点。

* 类似flume的应用
* 事件总线驱动
* 分为收集(collect)，分析(analyse)，转存输出(export)，三个核心逻辑模块
* 核心模块功能全部通过接口逻辑串联，可自行拓展各模块具体实现方式。
* 每个核心逻辑之间都设计有缓存，支持大量数据堆积，具有高可用性。
* 其余已实现的较重要的模块，还有网络传输，链式分析，状态监控等。
* 未实现的包含name server和notify组件
* 使用的技术包含，Spring boot,netty4，mongodb3.0，kryo，jackson等。

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) 2015-2016 leadin Group Holding Limited

##开发人员
1：tophawk PM+整体架构搭建+网络传输模块
2：liushuo 主数据分析和输出模块
3：xiaofei 主内存队列和事件总线模块
4：lixu 主file缓存模块
5：Jon.K 主要负责portal监控和展现模块

##时间节点
1：2015年3月，开发时间从需求到结束，约一个月。
2：2016年11月28日，server模块简单集成了spring boot，用spring data mongodb 替换原来的mongo工具类。
3：2016年11月30日上传