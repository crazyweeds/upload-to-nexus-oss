# upload-to-nexus-oss
Automatically upload a local repository to a remote repository(Nexus-OSS-3)|将本地仓库上传到远程仓库

## 一、版本说明

#### 理论支持所有支持maven `deploy` 的仓库；

## 二、原理说明

#### 起初考虑使用Nexus3的Rest接口，但考虑到兼容性，换成了`mvn deploy`形式。不仅仅开发简单，后续也几乎不用维护和迭代；
    
## 三、功能说明：
    
#### 将本地标准仓库，上传到maven私服，暂时只支持标准本地仓库（包含pom文件），支持上传源码包（无需配置），同时也支持纯pom依赖。
    
## 四、适用场景：

#### 本地有标准的maven仓库(文件夹)，需要批量上传到远程私有仓库，也就是离线开发的场景，手动上传几乎不可能，采用spring-boot的中小型项目，包含的jar包数量都成百上千，手动操作出错的几率太高。
    
## 五、不适用：

#### 只有jar包，没有pom文件。这种场景，jar文件数量一般应该不多，推荐手动上传，或者自己改造代码（实现更加简单，无非就是获取所有jar，如果是maven打包的，META-INF目录里面有pom文件，解析之类的就行了，如果没有，那只能人为操作了）。为什么不支持该场景呢？那是因为目前很多项目，都隐性依赖某些pom文件，这些依赖包内容只有pom，没有jar包。  

## 六、准备工作

#### 1.本地安装maven，并配置环境变量，能够正常执行`mvn`命令；

#### 2.编辑本地maven的`setting.xml`文件，增加`server`配置；

#### 3.通过`config.properties`配置参数，可参照项目中的`/resources/config.properties`，该文件也是默认配置

#### 4.如果你用的是nexus，请将maven私服的`Deployment policy`配置为`Allow`;


## 七、例子

#### 方式一：自己编译

    git clone https://github.com/crazyweeds/upload-to-nexus-oss.git
    cd upload-to-nexus-oss
    mvn package
    cd target 
    java -jar upload-to-nexus-oss-1.0-SNAPSHOT-jar-with-dependencies.jar ${your config.properties file path}
    
#### 方式二：下载已编译版本

    https://github.com/crazyweeds/upload-to-nexus-oss/releases
    
    
## 八、迭代计划

#### 无特殊情况，不再迭代，除非硬性需求或者使用中出现BUG


## 九、配置文件

    #deploy的线程数，默认20个，请根据自身电脑和网络情况配置。
    thread.count=20
    
    #本地仓库所在路径，解析groupId是根据pom文件内容获取的，所以路径是什么并不重要。有pom文件即可。
    local.repository.path=/Users/chenruibo/.m2/repository
    
    #maven settings.xml 配置文件中的servers节点下目标server的id
    repository.id=my-server
    
    #远程仓库路径，如果使用nexus，别选择错了，必须是host类型，不能是proxy类型
    target.repo.url=http://baidu:8081/repository/maven-releases/
    
    #deploy超时时间，单位是s。默认10分钟，正常情况，应该足够了
    deploy.timeout=600
