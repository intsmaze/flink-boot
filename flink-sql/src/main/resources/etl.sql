<?xml version="1.0" encoding="UTF-8"?>
<sqls>

  <!-- 查询每个循环根据seqid,testTime 去重下，第一条数据的stepNumber,stepType -->
	<sql id="sql_temp">
	  SELECT
	flowId,
	cycleNumber,
	stepNumber,
	stepType
FROM
			sourceData
	</sql>

</sqls>