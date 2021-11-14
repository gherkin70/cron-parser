package org.cron

import org.cron.substitutes.DayOfWeek
import org.cron.substitutes.FieldType
import org.cron.substitutes.Month

class CronParser {
    fun parseCron(cronString: String): String {
        val fields = cronString.split(" ")
        if (fields.size < 6) {
            return "5 fields plus a command is required"
        }
        val outputs = fields.subList(0, 5).mapIndexed { i, field ->
            val fieldType = FieldType.values()[i]
            parseField(field, fieldType)
        }
        return outputs.joinToString(separator = "\n") + "\ncommand       ${fields[5]}"
    }

    private fun parseField(field: String, type: FieldType): String {
        val params = field.split(",")
        val times = params.flatMap {
            generateTimes(it, type)
        }.distinct()
        val fieldName = type.displayName.padEnd(PAD_LENGTH)
        return fieldName + times.joinToString(separator = " ")
    }

    private fun generateTimes(param: String, type: FieldType): List<Int> {
        return if (param.length == 1) {
            if (param == "*") {
                type.possibleValues.map { it }
            } else {
                val value = param.toIntOrNull()
                if (value == null || !isValidValue(value, type)) {
                    error("Invalid param $param")
                }
                listOf(value)
            }
        } else if (param.length == 3 && param.all { it.isLetter() }) {
            listOf(convertAbbreviation(param, type))
        } else {
            val range = getRange(param, type)
            val stepValue = getStep(param)
            val steps = if (stepValue == null) range else range step stepValue
            steps.map { it }
        }
    }

    private fun convertAbbreviation(param: String, type: FieldType) = when (type) {
        FieldType.DAY_OF_MONTH -> Month.values().singleOrNull { it.name == param }?.numericValue
        FieldType.DAY_OF_WEEK -> DayOfWeek.values().singleOrNull { it.name == param }?.numericValue
        else -> null
    } ?: error("Invalid param $param")

    private fun getRange(
        param: String,
        fieldType: FieldType
    ) = RANGE_PATTERN.find(param)?.groupValues?.let {
        val startValue = it[1].toInt()
        val endValue = it[2].toInt()
        validateRange(startValue, endValue, fieldType)
        startValue..endValue
    } ?: error("Invalid range param $param")

    private fun getStep(
        param: String
    ) = STEP_PATTERN.find(param)?.groupValues?.let {
        it[2].toIntOrNull()
    }

    private fun validateRange(startValue: Int, endValue: Int, fieldType: FieldType) {
        if (!(isValidValue(startValue, fieldType) && isValidValue(endValue, fieldType))) {
            error("Invalid value range $startValue - $endValue for field type ${fieldType.displayName}")
        }
        if (startValue >= endValue) {
            error("Start of range $startValue should be < end of range $endValue")
        }
    }

    private fun isValidValue(value: Int, fieldType: FieldType) =
        value in fieldType.possibleValues

    companion object {
        private const val PAD_LENGTH = 14

        private val RANGE_PATTERN = Regex("([\\d]{1,2})-([\\d]{1,2})")
        private val STEP_PATTERN = Regex("(/([\\d]{1,2}))")
    }
}