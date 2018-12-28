# project_useraction
1、工程介绍（project_useraction:用户行为分析）
----------------------------------------------------------
 - metriccount_mr: PV UV计算MapReduce版本
 - metriccount_spark: PV UV计算Spark版本, 另外包含页面访问数统计和SparkSQL演示
 - useraction_monitor: 数据可视化展示 NodeJs + Vue.js + Echarts组成
            
2、如何运行该系统
----------------------------------------------------------
 - 确保已经安装Docker/Nginx/Flume/MySQL/Maven/MapReduce/Spark/Npm/NodeJs
 - 参考视频安装配置OpenCart
   - 在配置Nginx时请注意Nginx采集日志使用的log_format
     - log_format acc_log '$remote_addr\t$scheme://$host$request_uri\t$http_referer\t$uid_got\t$time_local';
 - 使用Flume采集数据到HDFS
 - 使用maven package编译metriccount_mr项目,在MapReduce上计算PV和UV
   - hadoop jar metriccount_mr-1.0-SNAPSHOT-jar-with-dependencies.jar com.gupao.bigdata.mapreduce.PVCount <input_path> <output_path>
   - hadoop jar metriccount_mr-1.0-SNAPSHOT-jar-with-dependencies.jar com.gupao.bigdata.mapreduce.UVCount <input_path> <output_path>
 - 使用maven package编译metriccount_spark项目,在Spark上计算PV和UV
   - spark-submit --master yarn-client --class metriccount_spark-1.0-SNAPSHOT-uber.jar <input_path> <output_path>
   - spark-submit --master yarn-client --class metriccount_spark-1.0-SNAPSHOT-uber.jar <input_path> <output_path>
 - 在mysql上初始化db
   - 执行useraction_monitor/sql/init.sql
 - 使用Sqoop同步数据到MySQL
   - sqoop export --connect <jdbc_url> \
   --username <user_name> --password <password> \
   --table <table_name> -m 1 \
   --export-dir <export_dir> \
   --fields-terminated-by '\t'
 - 使用npm运行useraction_monitor项目,读取MySQL的数据进行可视化展示
   - 配置mysql连接: useraction_monitor/src/server/config/mysql_config.js
   - npm install
   - npm run dev
