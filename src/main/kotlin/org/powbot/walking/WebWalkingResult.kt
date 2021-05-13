package org.powbot.walking

enum class FailureReason {
    NoPath, FailedInteract, CantReachNextNode, CantLoadPlayer, ExceptionThrown, TargetNull, Unknown
}

data class WebWalkingResult(val usedWeb: Boolean,
                            val success: Boolean,
                            val failureReason: FailureReason?)
