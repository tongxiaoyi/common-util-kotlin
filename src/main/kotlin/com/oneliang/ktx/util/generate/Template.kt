package com.oneliang.ktx.util.generate

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.file.FileUtil
import com.oneliang.ktx.util.json.JsonUtil
import com.oneliang.ktx.util.logging.LoggerManager
import javax.script.Invocable
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class Template {
    companion object {
        private val logger = LoggerManager.getLogger(Template::class)
        private val scriptEngine: ScriptEngine

        init {
            val scriptEngineManager = ScriptEngineManager()
            scriptEngine = scriptEngineManager.getEngineByExtension("js")
        }
    }

    fun generate(parameter: Parameter) {
        try {
            val stringBuilder = StringBuilder()
            FileUtil.readFileContentIgnoreLine(parameter.templateFile, Constants.Encoding.UTF8, object : FileUtil.ReadFileContentProcessor {
                override fun afterReadLine(line: String): Boolean {
                    stringBuilder.append(line)
                    stringBuilder.append(Constants.String.CRLF_STRING)
                    return true
                }
            })
            val templateContent = stringBuilder.toString()
            val bindings = scriptEngine.createBindings()
            scriptEngine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE)
            var json: String = Constants.String.BLANK
            val instance = parameter.instance
            if (instance != null) {
                json = JsonUtil.objectToJson(instance, emptyArray(), parameter.jsonProcessor)
            } else {
                if (parameter.json.isNotBlank()) {
                    json = parameter.json
                }
            }
            logger.debug("data object json:$json")
            scriptEngine.eval(JavaScriptFunctionGenerator.getObject(json))
            scriptEngine.eval(JavaScriptFunctionGenerator.template())
            val invocable = scriptEngine as Invocable
            logger.debug(templateContent)
            var functionResult = invocable.invokeFunction(JavaScriptFunctionGenerator.FUNCTION_TEMPLATE, templateContent)
            logger.debug(JavaScriptFunctionGenerator.getResult(functionResult.toString()))
            scriptEngine.eval(JavaScriptFunctionGenerator.getResult(functionResult.toString()))
            functionResult = invocable.invokeFunction(JavaScriptFunctionGenerator.FUNCTION_GET_RESULT)
            val toFile = parameter.toFile
            val toFileByteArray = if (functionResult != null && functionResult.toString().isNotBlank()) {
                logger.debug(functionResult.toString())
                functionResult.toString().toByteArray(Charsets.UTF_8)
            } else {
                logger.debug(templateContent)
                stringBuilder.toString().toByteArray(Charsets.UTF_8)
            }
            FileUtil.writeFile(toFile, toFileByteArray)
        } catch (e: Exception) {
            logger.error(Constants.Base.EXCEPTION, e)
        }
    }

    class Parameter {
        var templateFile: String = Constants.String.BLANK
        var toFile: String = Constants.String.BLANK
        var instance: Any? = null
        var json: String = Constants.String.BLANK
        var jsonProcessor: JsonUtil.JsonProcessor = JsonUtil.DEFAULT_JSON_PROCESSOR

    }

    private object JavaScriptFunctionGenerator {
        const val FUNCTION_TEMPLATE = "template"
        const val FUNCTION_GET_OBJECT = "getObject"
        const val FUNCTION_GET_RESULT = "getResult"
        fun template(): String {
            val stringBuilder = StringBuilder()
            val hashCode = stringBuilder.hashCode()
            val startKey = "@${hashCode}S@"
            val endKey = "@${hashCode}E@"
            stringBuilder.append("function $FUNCTION_TEMPLATE(string){")
            stringBuilder.append("string=string.replace(/[\\r]/g, \"\\\\r\");")
            stringBuilder.append("string=string.replace(/[\\t]/g, \"\\\\t\");")
            stringBuilder.append("string=string.replace(/[\\n]/g, \"\\\\n\");")
            stringBuilder.append("string=string.split(\"<%\").join(\"$startKey\");")
            stringBuilder.append("string=string.replace(/((^|%>)[^$startKey]*)'/g, \"$1$endKey\");")
            stringBuilder.append("string=string.replace(/$startKey=(.*?)%>/g, \"',$1,'\");")
            stringBuilder.append("string=string.split(\"$startKey\").join(\"');\");")
            stringBuilder.append("string=string.split(\"%>\").join(\"p.push('\");")
            stringBuilder.append("string=string.split(\"$endKey\").join(\"\\'\");")
            stringBuilder.append("return \"var p=[];var object=$FUNCTION_GET_OBJECT();if(object!==null){with(object){p.push('\" +string+ \"');}}return p.join('');\"")
            stringBuilder.append("}")
            return stringBuilder.toString()
        }

        fun getObject(json: String): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("function $FUNCTION_GET_OBJECT(){")
            stringBuilder.append("var object=$json;")
            stringBuilder.append("return object;")
            stringBuilder.append("}")
            return stringBuilder.toString()
        }

        fun getResult(string: String): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("function $FUNCTION_GET_RESULT(){")
            stringBuilder.append(string)
            stringBuilder.append("}")
            return stringBuilder.toString()
        }
    }
}