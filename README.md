# upload-to-nexus-oss
Automatically upload a local repository to a remote repository(Nexus-OSS-3)|将本地仓库上传到远程仓库

## 版本说明

    支持：nexus oss 3+
    不支持：nexus oss 2


## 参考文档：

    多谢官方提供的文档，虽然不太全，但也足够了。

    https://help.sonatype.com/repomanager3/rest-and-integration-api/components-api
    
## 功能说明：
    
    将指定目录的本地仓库，批量上传到nexus-oss中。支持pom(比如spring-boot-parent)，更支持jar(比如spring-webmvc)。
    
## 适用场景：

    本地有标准的maven仓库，需要大批量上传的私有仓库，说白了就是断网开发的场景，手动上传几乎不可能，采用spring-boot的中小型项目，包含的jar包数量都成百上千，手动操作出错的几率太高。
    
## 不适用：

    只有jar包，没有pom文件。这种场景，jar一般应该不多，推荐手动上传，或者自己改造代码（实现更加简单，无非就是获取所有jar，如果是maven打包的，META-INF目录里面有pom文件，解析之类的就行了，如果没有，那只能人为操作了）。
