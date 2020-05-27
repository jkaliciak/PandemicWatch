package dev.jakal.pandemicwatch

import dev.jakal.pandemicwatch.common.utils.formatDecimalPoints
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class FormatHelperTest : WordSpec() {

    init {

        "FormatHelper" When {

            "called Double.formatDecimalPoints() 13.37666 and 0 decimal point" should {

                "format to 13" {
                    13.37666.formatDecimalPoints(0) shouldBe "13"
                }
            }

            "called Double.formatDecimalPoints() 13.37666 and 1 decimal point" should {

                "format to 13.3" {
                    13.37666.formatDecimalPoints(1) shouldBe "13.4"
                }
            }

            "called Double.formatDecimalPoints() 13.37666 and 2 decimal point" should {

                "format to 13.38" {
                    13.37666.formatDecimalPoints(2) shouldBe "13.38"
                }
            }

            "called Double.formatDecimalPoints() 13.37666 and 3 decimal point" should {

                "format to 13.377" {
                    13.37666.formatDecimalPoints(3) shouldBe "13.377"
                }
            }
        }
    }
}