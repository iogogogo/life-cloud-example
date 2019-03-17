# 多节点注册中心，伪集群

### 修改 /etc/hosts 文件

```shell
vim /etc/hosts
# 添加以下内容
127.0.0.1 peer1
127.0.0.1 peer2
127.0.0.1 peer3
```

### 启动时指定启动参数

```shell
java -jar cloud-eureka-0.0.1.jar --spring.profiles.active=peer1
java -jar cloud-eureka-0.0.1.jar --spring.profiles.active=peer2
java -jar cloud-eureka-0.0.1.jar --spring.profiles.active=peer3
```

