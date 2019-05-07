#!/bin/bash

# 指定根目录，请按实际修改
BLOG_DIR="/www/ms-blog/ms-blog"
# 拉取最新的源码
# git pull
echo "代码拉取完毕！"

# 进入SENS根目录
cd $BLOG_DIR

# 停止SENS
sh $BLOG_DIR/bin/blog.sh stop

# 执行打包
mvn clean package -Dmaven.test.skip=true
echo "代码拉取完毕！"

# 进入打包好的目录
cd $BLOG_DIR/target

# 运行jar       2>&1 是将标准出错重定向到标准输出，这里指输出全部扔掉
nohup java -server -Xms256m -Xmx512m -jar `find ./ -name "ms-blog*.jar"` > /dev/null 2>&1 &

echo "ms-blog部署完毕"