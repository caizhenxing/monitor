描述：
一个基于Spring AOP + redis + echarts 实现的方法性能监控插件。

使用方式：
1、通过注解形式
2、通过页面管理


一、redis使用pipeline lpush  与 pipeline zadd的考量
考虑到pipeline可以直接返回结果不需要等待，再配合lpush可以保证数据的有序性。
zadd可以设置score，使用时间作为score排序可以满足时间有序性。

