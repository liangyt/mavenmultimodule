### Maven 多模块项目示例
> 由于各个IDE创建maven项目的过程有一些不同，所以这里就不列出创建过程截图了，以文字说明。

maven的项目结构在各个不同的IDE下都是一样的：
```apple js
project
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   └── resources
    └── test
        └── java

```
首先创建父模块的maven项目，然后再在父模块下创建子模块，父模块项目的src目录可以删除掉，一般不会使用，创建完成的示例结构大概如下:
```apple js
├── pom.xml
├── consumer
│   ├── consumer.iml
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   └── resources
│       └── test
│           └── java
├── provide
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   └── resources
│       └── test
│           └── java
└── service
    ├── pom.xml
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   └── resources
    │   └── test
    │       └── java

```

上面的结构是有三个子模块。  
下面看看父模块的POM配置数据：
```apple js
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!--父模块的 groupId、artifactId、version，子模块可以通过parent继承父模块，使用的就是这里定义的value-->
    <groupId>com.liangyt</groupId>
    <artifactId>maven-multi-module</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--根pom必须要有-->
    <packaging>pom</packaging>
    <!--子模块-->
    <modules>
        <module>service</module>
        <module>provide</module>
        <module>consumer</module>
    </modules>

    <properties>
        <java-version>1.8</java-version>
        <service-version>1.0-SNAPSHOT</service-version>
        <spring-boot-version>1.5.4.RELEASE</spring-boot-version>
    </properties>

    <!--
        使用dependencyManagement的话，则子模块不会自动继承父POM依赖；
        需要子模块显示定义需要的依赖，如果不写 version 则使用父POM定义的version,如果子模块的依赖写了version则使用子模块的。
    -->
    <dependencyManagement>
        <dependencies>
            <!--该依赖里面其实还包含了很多其它的依赖，这里只是为了示范，生产环境可以细化，只添加需要的依赖-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <version>${spring-boot-version}</version>
            </dependency>
            <!--定义子模块包，其他子模块可以引用-->
            <dependency>
                <groupId>com.liangyt</groupId>
                <artifactId>service</artifactId>
                <version>${service-version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java-version}</source>
                    <target>${java-version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
配置了两个子模块的POM,provide引用了service模块,service和provide配置信息如下：
```apple js
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!--继承的父模块-->
    <parent>
        <artifactId>maven-multi-module</artifactId>
        <groupId>com.liangyt</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <!--一般跟父模块下的模块名一致，也是其它模块引用的名称-->
    <artifactId>service</artifactId>
    <!--
        发版的版本号,如果重新发布了新的版本，则父模块添加的该依赖需要新的版本的话还需要相应的修改一下才能引用新的发布包
    -->
    <version>1.0-SNAPSHOT</version>

    <packaging>jar</packaging>

    <!--
        需要哪个依赖就添加哪个依赖：
        如果父模块里面有该依赖的话，则只要不填写版本则跟父模块版本一样；
        如果写了版本号，则跟父模块没有什么关系；
    -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```
```apple js
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>maven-multi-module</artifactId>
        <groupId>com.liangyt</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>provide</artifactId>

    <dependencies>
        <!--引用service模块-->
        <dependency>
            <groupId>com.liangyt</groupId>
            <artifactId>service</artifactId>
        </dependency>
    </dependencies>
</project>
```

进到service目录执行以下语句则把service打包了，并放到仓库下：
> mvn install

这个如果给provide打包的话则会自动把service包添加进去了。