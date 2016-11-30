## leanin 项目实现功能和特点。

* 类似flume的应用
* 事件总线驱动
* 分为收集(collect)，分析(analyse)，转存输出(export)，三个核心逻辑模块
* 核心模块功能全部通过接口逻辑串联，可自行实现各模块具体实现方式。
* 每个核心逻辑之间都设计有缓存，支持大量数据堆积，具有高可用性。
* 其余已实现的较重要的模块，还有网络传输，链式分析，状态监控等。
* 使用的技术包含，Spring boot,netty4，mongodb3.0，kryo，jackson等。


## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) 2015-2016 leadin Group Holding Limited
