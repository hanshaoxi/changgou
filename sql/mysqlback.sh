#!/bin/bash

#功能说明：本功能用于备份笔记数据库



#数据库名
dbname=leanote


#备份时间

backtime=`date +%Y-%m-%d-%H%M%S`

#日志备份路径

logpath=/root/bak_data/mongo/backup

#数据备份路径

datapath=/root/bak_data/mongo/backup

#日志记录头部

echo ‘”备份时间为${backtime},备份数据库表 ${dbname} 开始” >> ${logpath}/leanotelog.log

#正式备份数据库

mongodump -h localhost -o ${datapath}



#为节约硬盘空间，将数据库压缩
echo "开始压缩备份的数据库文件leanote"


tar jcf ${datapath}/leanote-${backtime}.tar.bz2 ${datapath}/leanote > /dev/null

#删除原始文件，只留压缩后文件

rm -f /root/bak_data/leanote

echo “数据库表 ${dbname} 备份成功!!” >> ${logpath}/leanotelog.log

else

#备份失败则进行以下操作

echo “数据库 ${dbname} 备份失败!!” >> ${logpath}/leanotelog.log

fi

done
