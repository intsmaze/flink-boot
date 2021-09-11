package com.intsmaze.flink.groovy;

import groovy.lang.Binding;
import groovy.lang.Script;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

public class GroovyRuleCalc {

	protected final Logger logger = LoggerFactory.getLogger(GroovyRuleCalc.class);

	/**
	* @author:YangLiu
	* @date:2018年5月21日 下午9:50:03 
	* @describe:执行指定的脚本
	 */
	public boolean executeRuleCalc(GroovyScriptExecutor scriptExecutor,
			Binding shellContext, long ruleId) {
		boolean ruleIsMatched = false;
		String logtag = "";
		try {
			// groovy脚本规则
			// *********************配置脚本核心计算处*******************
			ConcurrentMap<Long, Pair<String, Script>> map = CacheGroovyService
					.getScriptCache();
			Iterator<Entry<Long, Pair<String, Script>>> entries = map
					.entrySet().iterator();
			while (entries.hasNext()) {
				Entry<Long, Pair<String, Script>> entry = entries.next();
				if (entry.getKey() == ruleId) {
					Pair<String, Script> pair = entry.getValue();
					Script script = pair.getRight();
					synchronized (script) {
						script.setBinding(shellContext);
						logger.info(
								"script is : {}.",
								CacheGroovyService.ScriptTextmap.get(entry.getKey()));
						ruleIsMatched = scriptExecutor.execute(script);

						logtag = String.valueOf(shellContext.getVariable("LOGTAG"));
						logger.info(logtag);
					}
				}
			}
			// *********************配置脚本核心计算处*******************
		} catch (Exception e) {
			logger.error("", e);
		} finally {
		}
		return ruleIsMatched;
	}

	/**
	 * @author:YangLiu
	 * @date:2018年5月7日 下午7:44:32
	 * @describe:执行所有的脚步
	 */
	public boolean executeRuleCalcAll(GroovyScriptExecutor scriptExecutor,
			Binding shellContext) {
		boolean ruleIsMatched = false;
		String logtag = "";
		try {
			// groovy脚本规则
			// *********************配置脚本核心计算处*******************
			ConcurrentMap<Long, Pair<String, Script>> map = CacheGroovyService
					.getScriptCache();

			Iterator<Entry<Long, Pair<String, Script>>> entries = map
					.entrySet().iterator();
			while (entries.hasNext()) {

				Entry<Long, Pair<String, Script>> entry = entries.next();
				Pair<String, Script> pair = entry.getValue();
				Script script = pair.getRight();
				synchronized (script) {
					script.setBinding(shellContext);
					logger.info(
							"script is : {}.",
							CacheGroovyService.ScriptTextmap.get(entry.getKey()
									+ "a"));
					ruleIsMatched = scriptExecutor.execute(script);
					logtag = String.valueOf(shellContext.getVariable("logtag"));
					logger.info(logtag);
				}

			}
			// *********************配置脚本核心计算处*******************
		} catch (Exception e) {
			logger.error("", e);
		} finally {
		}
		return ruleIsMatched;
	}

}
