package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun testConvertDollarToEuro() {
        val dollars = 100
        val expectedEuro = 93

        val euro = Utils.convertDollarToEuro(dollars)

        assertEquals(expectedEuro, euro)
    }

    @Test
    fun testConvertEuroToDollar() {
        val euros = 100
        val expectedDollar = 108

        val dollar = Utils.convertEuroToDollar(euros)

        assertEquals(expectedDollar, dollar)
    }

    @Test
    fun testTodayDate() {
        val expectedDateFormat = "dd/MM/yyyy"

        val expectedDate = SimpleDateFormat(expectedDateFormat, Locale.getDefault()).format(Date())
        val actualDate = Utils.todayDate

        assertEquals(expectedDate, actualDate)
    }
}