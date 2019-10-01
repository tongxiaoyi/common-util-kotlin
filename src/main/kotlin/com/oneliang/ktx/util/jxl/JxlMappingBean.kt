package com.oneliang.ktx.util.jxl

import java.util.concurrent.CopyOnWriteArrayList

class JxlMappingBean {
    companion object {
        const val TAG_BEAN = "bean"
        const val USE_FOR_IMPORT = "import"
        const val USE_FOR_EXPORT = "export"
    }
    /**
     * @return the useFor
     */
    /**
     * @param useFor the useFor to set
     */
    var useFor = USE_FOR_IMPORT
    /**
     * @return the type
     */
    /**
     * @param type the type to set
     */
    var type: String? = null
    val jxlMappingColumnBeanList = CopyOnWriteArrayList<JxlMappingColumnBean>()

    /**
     * get header
     * @param field
     * @return header
     */
    fun getHeader(field: String?): String? {
        var header: String? = null
        if (field != null) {
            for (jxlMappingColumnBean in jxlMappingColumnBeanList) {
                val columnField = jxlMappingColumnBean.field
                if (columnField == field) {
                    header = jxlMappingColumnBean.header
                    break
                }
            }
        }
        return header
    }

    /**
     * get field
     * @param header
     * @return field
     */
    fun getField(header: String?): String? {
        var field: String? = null
        if (header != null) {
            for (jxlMappingColumnBean in jxlMappingColumnBeanList) {
                val columnHeader = jxlMappingColumnBean.header
                if (columnHeader == header) {
                    field = jxlMappingColumnBean.field
                    break
                }
            }
        }
        return field
    }

    /**
     * get index
     * @param field
     * @return field index
     */
    fun getIndex(field: String?): Int {
        var index = -1
        if (field != null) {
            for (jxlMappingColumnBean in jxlMappingColumnBeanList) {
                val columnField = jxlMappingColumnBean.field
                if (columnField != null && columnField == field) {
                    index = jxlMappingColumnBean.index
                    break
                }
            }
        }
        return index
    }

    /**
     * @param jxlMappingColumnBean
     * @return boolean
     */
    fun addJxlMappingColumnBean(jxlMappingColumnBean: JxlMappingColumnBean): Boolean {
        return this.jxlMappingColumnBeanList.add(jxlMappingColumnBean)
    }
}
