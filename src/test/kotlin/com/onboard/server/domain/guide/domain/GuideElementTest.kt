package com.onboard.server.domain.guide.domain

import com.onboard.server.domain.guide.exception.CannotDuplicateSequenceException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec

class GuideElementTest : DescribeSpec({
    describe("checkSequenceUnique") {
        it("가이드 요소의 순서는 중복되면 안된다") {
            val sequences = listOf(1, 2, 3, 4, 5, 6, 7)

            shouldNotThrowAny {
                GuideElement.checkSequenceUnique(sequences)
            }
        }

        context("가이드 요소의 순서가 중복이면") {
            val duplicateSequences = listOf(1, 1, 2, 2, 3, 3)

            it("예외가 발생한다") {
                shouldThrow<CannotDuplicateSequenceException> {
                    GuideElement.checkSequenceUnique(duplicateSequences)
                }
            }
        }
    }
})
