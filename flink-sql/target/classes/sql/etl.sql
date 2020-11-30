<?xml version="1.0" encoding="UTF-8"?>
<sqls>

  <!-- 查询每个循环根据seqid,testTime 去重下，第一条数据的stepNumber,stepType -->
	<sql id="sql_temp">
	  SELECT
	main.flowId,
	main.cycleNumber,
	main.total,
	sourceData.stepNumber,
	sourceData.stepType
FROM
	(
		SELECT
			flowId,
			cycleNumber,
			count(1) AS total,
			TUMBLE_ROWTIME (
				receiveTime,
				INTERVAL '5' SECOND
			) AS bewteenTime
		FROM
			sourceData
		GROUP BY
			TUMBLE (
				receiveTime,
				INTERVAL '5' SECOND
			),
			flowId,
			cycleNumber
	) main
LEFT JOIN sourceData ON main.uid = sourceData.uuid
AND sourceData.receiveTime &lt;= main.bewteenTime
AND sourceData.receiveTime &gt;= main.bewteenTime - INTERVAL '5' SECOND
	</sql>

</sqls>