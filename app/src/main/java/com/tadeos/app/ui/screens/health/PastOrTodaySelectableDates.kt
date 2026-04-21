package com.tadeos.app.ui.screens.health

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import com.tadeos.app.data.validation.HealthRecordValidator
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
class PastOrTodaySelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return HealthRecordValidator.isSelectablePickerDate(utcTimeMillis)
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= Calendar.getInstance().get(Calendar.YEAR)
    }
}
