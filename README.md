## 环境及工具准备（windows使用64位操作系统,jdk对应下载64位）
##########################执行###############################
- Java开发IDE（windows）：

      IntelliJ IDEA Community Edition 2023.2.3（可选择自己的）

- C++开发IDE（windows）:  

      VisualStudio 2022

-  windows openjdk8下载地址：

       https://www.openlogic.com/openjdk-downloads?field_java_parent_version_target_id=416&field_operating_system_target_id=436&field_architecture_target_id=391&field_java_package_target_id=396

- linux jdk8下载地址（或使用yum/apt等命令直接下载安装）：
  
       https://www.oracle.com/java/technologies/downloads/#java8

# 创建dll/so示例

- 创建dll示例

1. DLL创建(demodll)

新建项目—>Visual C++—>Windows桌面—>动态链接库，修改dll名称、位置
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/e9ec47d8-0a3b-4871-9ac9-f8161a4d1295)

2. 添加源文件mydll.cpp
   
解决方案资源管理器->项目（demodll）->源文件->右键添加->新建项->mydll.cpp
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/d8a9d3ce-092e-4d23-aadd-df5df397b5b6)
3. 复制demo代码
<div>
   //要导出一个全局函数，就用关键字_declspec(dllexport)来声明 </br>
   //下边是一个把两个变量相加的函数，相信大家都看得懂，extern "C" 保证java调用是函数名称不变</br>
   extern "C" _declspec(dllexport)</br>
   int add(int a, int b)</br>
   {</br>
	   return a + b;</br>
   }</br>
</div>

4. 选择X64/X86、Release/Debug编译选项后，设置不使用预编译头：项目->属性—>C/C++—>预编译头，否则可能会报错。我选择的是X64、Debug

![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/8180b4b5-8e5c-45e0-80bb-3488d00de700)
点击菜单栏中项目或者再解决方案资源管理器中右键当前项目都可以，设置成不使用预编译头
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/e2e57f31-9ed6-48bd-91e2-f3b56a031fce)
同时代码生成选择/MTd
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/04ac10a2-4cf1-4e85-a419-6fe0a5f6a198)

5. 右键解决方案资源管理器中当前项目，选择运行，或者直接运行
   
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/bf30b7d0-2b9d-4daf-ac29-b756fddead1c)
可查看生成的dll

6. vs中验证dll是否正确
   
<h>创建项目Project，将.dll文件放在该项目.vcxproj和.cpp同级目录下,lib目录需自己创建</h>
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/172a9c44-5f05-4cab-85bb-700ae657cd7f)
`选择X64/X86、Release/Debug编译选项后，将.lib文件放在项目lib文件夹（如上图，lib文件夹需要自己创建，名称随便）中，并将该路径添加到：属性—>VC++目录—>库目录中`
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/8f4a1eb9-5ab0-494f-9d1c-557f50af23da)

# 源文件中添加MarkingLabel.cpp 
<div>
#include <iostream> </br>
//声明库 </br>
#pragma comment(lib,"demodll.lib") </br>
//声明，此函数要在dll导入 </br>
extern "C" _declspec(dllexport) int add(int a, int b); </br>
int main() </br>
{ </br>
	std::cout << "Hello World!\n"; </br>
	printf("%d\n", add(1, 2)); </br>
	system("pause"); </br>
	//std::cout << avcodec_configuration() << std::endl; </br>
} </br>
</div>
    
# 点击运行
![Image](https://github.com/users/zhangxiaodong1209/projects/1/assets/44725649/d6e7c795-e1ac-4c82-b86e-496a9f94e195)



#########################################################


## 创建so示例 
# jni 方式生成so动态库
#########################################################
# Test.java的内容

public class Test { </br>
    public native void printHelloWorld(); </br>
    public static void main(String[] args) { </br>
    System.loadLibrary("HelloWorld"); </br>
    Test test = new Test(); </br>
    test.printHelloWorld(); </br>
} </br>

- <h> CMD中执行javah Test命令 取得Test.h，可以在windows环境中取得，放入到linux中</h>

- <h> 指定jdk中 jni.h 及 jni_md.h进行编译（文件在jdk安装目录下，jdk\bin\include及jdk\bin\include\linux下），注意在linux环境中进行</h>
# helloWorld.cpp 内容

#include "Test.h" </br>
#include <iostream> </br>

JNIEXPORT void JNICALL Java_Test_printHelloWorld(JNIEnv *env, jobject obj) { </br>
  printf("Hello, World!\n"); </br>
} </br>

#########################################################
- linux中编译cpp文件生成so动态库文件-I为jni.h 及 jni_md.h的目录，Test.h放置在与helloWorld.cpp同级目录中，生成文件名称应lib开头
# compile.sh 内容

#!/bin/bash </br>
g++ -shared -fPIC -o libHelloWorld.so helloWorld.cpp -I/root/test/java/jdk1.8.0_401/include -I/root/test/java/jdk1.8.0_401/include/linux </br>

#########################################################
## centOS安装g++工具（install g++）,其他如ubuntu可使用apt等命令

sudo yum install -y http://mirror.centos.org/centos/7/extras/x86_64/Packages/centos-release-scl-rh-2-3.el7.centos.noarch.rpm </br>

sudo yum install -y http://mirror.centos.org/centos/7/extras/x86_64/Packages/centos-release-scl-2-3.el7.centos.noarch.rpm </br>

sudo yum install devtoolset-9-gcc-c++ </br>

source /opt/rh/devtoolset-9/enable </br>

g++ --version </br>

#########################################################
# jna 方式生成so动态库
#########################################################
## hello.cpp 内容
#include <iostream> </br>
  
extern "C" { </br>
    void myFunction() { </br>
            sprintf("Hello from C++ function!"); </br>
    } </br>
} </br>

#########################################################
# compile.sh 内容
#!/bin/bash </br>
g++ -shared -fPIC -o libDemo.so hello.cpp </br>

执行chmod +x compile.sh </br>
执行./compile.sh </br>

## 创建Java示例
########################################################
如代码，内包含已生成好的dll和so(均64位)
# idea中导出jar
# 配置
`如图依次点击File->Project Structure` </br>
![image](https://github.com/zhangxiaodong1209/JnaDemo/assets/44725649/68b55762-7aa1-4db2-9af1-2d43814aadbc)
 </br>
`如图依次点击Artifacts->+` </br>
![image](https://github.com/zhangxiaodong1209/JnaDemo/assets/44725649/9f13f7a4-abbc-4e53-bd00-a3f58a0b8cd8)
# 导出
`如图依次点击Build->Build Artifacts,选择，确定即可` </br>
![image](https://github.com/zhangxiaodong1209/JnaDemo/assets/44725649/f4194593-974a-4359-8adf-aa5d652f9cd9)

## 验证
##########################执行###############################
# windows环境javaIDE中可以执行调用dll
`Idea 中添加jna.library.path参数` </br>
`如图依次点击Run->Edit configurations->Modify options->add VM options` </br>
`添加"-Djna.library.path=D:\workspace\demodll\demodll\x64\Debug -Djna.debug_load=true",jna.library.path值为dll存放地址` </br>
![image](https://github.com/zhangxiaodong1209/JnaDemo/assets/44725649/656839fa-2ebc-4ff3-888c-3545a353fe62)
![image](https://github.com/zhangxiaodong1209/JnaDemo/assets/44725649/abd49afb-058d-45e6-b715-81b6e21d4df2)
 </br>
`点击Run,运行代码中的DemoDll类即可` </br>

##########################执行###############################
# 命令行执行jar
- jni动态库调用
java  -Djava.library.path=. -jar demo1.jar
- jna动态库调用
java  -Djna.library.path=. -Djna.debug_load=true -jar demo1.jar nnn

- 查看so函数是否正确
</br>
nm -D libDemo.so
</br>
nm -D libHelloWorld.so
