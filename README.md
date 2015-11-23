一、redis使用pipeline lpush  与 pipeline zadd的考量
考虑到pipeline可以直接返回结果不需要等待，再配合lpush可以保证数据的有序性。
zadd可以设置score，使用时间作为score排序可以满足时间有序性。

