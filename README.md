# upload-to-nexus-oss
Automatically upload a local repository to a remote repository(Nexus-OSS-3)|将本地仓库上传到远程仓库

## 版本说明

#### 理论支持所有支持maven `deploy` 的仓库；

    
## 功能说明：
    
#### 将本地标准仓库，上传到maven私服，暂时只支持标准本地仓库（包含pom文件）。
    
## 适用场景：

#### 本地有标准的maven仓库，需要大批量上传的私有仓库，说白了就是断网开发的场景，手动上传几乎不可能，采用spring-boot的中小型项目，包含的jar包数量都成百上千，手动操作出错的几率太高。
    
## 不适用：

#### 只有jar包，没有pom文件。这种场景，jar一般应该不多，推荐手动上传，或者自己改造代码（实现更加简单，无非就是获取所有jar，如果是maven打包的，META-INF目录里面有pom文件，解析之类的就行了，如果没有，那只能人为操作了）。

## 准备

#### 1.本地安装maven，并配置环境变量，能够正常执行`mvn`命令；

#### 2.编辑本地maven的`setting.xml`文件，增加`server`配置；

#### 3.通过`config.properties`配置参数

#### 4.如果你用的是nexus，请将maven私服的`Deployment policy`配置为`Allow`;


## 例子

#### 方式一：自己编译

    git clone https://github.com/crazyweeds/upload-to-nexus-oss.git
    cd upload-to-nexus-oss
    mvn package
    cd target 
    java -jar upload-to-nexus-oss-1.0-SNAPSHOT-jar-with-dependencies.jar ${your config.properties file path}
    
#### 方式二：下载已编译版本

    https://github.com/crazyweeds/upload-to-nexus-oss/releases
    
    
