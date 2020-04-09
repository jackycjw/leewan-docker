# leewan-docker
一个管理Docker容器的工具

玩了一段时间的K8S，感觉整体太复杂，就决定自己搞一个docker容器管理系统，管理的方式主要基于docker和registry的Restful接口。

整体分为两个工程，
  1、manage管理中心：负责整体的调度和管理任务
  2、worker执行器： 收集机器信息上报管理中心，执行管理中心的指令
