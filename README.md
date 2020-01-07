# OA
### MySQL URL 参数说明（勿删）
#### jdbc:mysql://172.16.213.210:3306/jaoauseSSL=false&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&useTimezone=true&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull
----------------------------------------------
参数名|参数值|说明

------------------------------------------------
                    -|                  -|      -|
serverTimezone       |     GMT%2B8       |    设置时区(与实际时间出现误差)
useTimezone          |     true          |    主要用在web服务器和数据库部署在不同时区时,转换二者交互中时间数据
useSSL               |     false         |    避免出现该错误或警告[According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. ]
useUnicode           |     true          |    使用Unicode编码
characterEncoding    |     utf8          |    指定编码格式
allowMultiQueries    |     true          |    开启MySQL批量执行SQL
zeroDateTimeBehavior |     convertToNull |    防止数据库中的日期格式为 0000-00-00 00:00:00 导致查询报错
autoReconnect        |     true          |    防止链接突然中断，开启自动重连功能
maxReconnects        |     5             |    最大重连次数

--------
[MYSQL DOC]:[https://dev.mysql.com/doc/refman/5.5/en/general-security-issues.html]


