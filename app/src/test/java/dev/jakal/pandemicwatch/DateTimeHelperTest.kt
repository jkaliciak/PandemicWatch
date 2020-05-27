package dev.jakal.pandemicwatch

import dev.jakal.pandemicwatch.common.utils.*
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month

class DateTimeHelperTest : WordSpec() {

    init {

        "DateTimeHelper" When {
            val localDate = LocalDate.ofYearDay(2020, 7)
            val localDateTime = LocalDateTime.of(2020, Month.JANUARY, 7, 1, 1, 1)

            "called LocalDate.toLong() for 07.01.2020" should {

                "map to 18268 epoch days" {
                    localDate.toLong() shouldBe 18268L
                }
            }

            "called Long.toLocalDate() for 18268 epoch days" should {

                "map to local date for 07.01.2020" {
                    18268L.toLocalDate() shouldBe localDate
                }
            }

            "called LocalDateTime.toLong() for 07.01.2020 01:01.11" should {

                "map to 1578358861000 epoch millis" {
                    localDateTime.toLong() shouldBe 1578358861000L
                }
            }

            "called Long.toLocalDateTime() for 1578358861000 epoch millis" should {

                "map to local date time for 07.01.2020 01:01.11" {
                    1578358861000L.toLocalDateTime() shouldBe localDateTime
                }
            }

            "called String.toLocalDateFromNovelCovidAPIDate() for 1/7/20" should {

                "map to local date for 07.01.2020" {
                    "1/7/20".toLocalDateFromNovelCovidAPIDate() shouldBe localDate
                }
            }

            "called LocalDateTime.toDateTimeFormat() for 1/7/20 01:01.01" should {

                "map to '1/7/20 1:01 AM'" {
                    localDateTime.toDateTimeFormat() shouldBe "1/7/20 1:01 AM"
                }
            }

            "called LocalDate.toDateFormat() for 1/7/20" should {

                "map to '7 Jan'" {
                    localDate.toDateFormat() shouldBe "7 Jan"
                }
            }
        }
    }
}