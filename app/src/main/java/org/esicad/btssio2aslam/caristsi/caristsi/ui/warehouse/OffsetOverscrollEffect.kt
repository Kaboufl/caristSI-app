package org.esicad.btssio2aslam.caristsi.caristsi.ui.warehouse

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
class OffsetOverscrollEffect(val scope: CoroutineScope) : OverscrollEffect {
    private val overscrollOffset = Animatable(0f)

    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset {
        // in pre scroll we relax the overscroll if needed
        // relaxation: when we are in progress of the overscroll and user scrolls in the
        // different direction = substract the overscroll first
        val sameDirection = sign(delta.y) == sign(overscrollOffset.value)
        val consumedByPreScroll = if (abs(overscrollOffset.value) > 0.5 && !sameDirection) {
            val prevOverscrollValue = overscrollOffset.value
            val newOverscrollValue = overscrollOffset.value + delta.y
            if (sign(prevOverscrollValue) != sign(newOverscrollValue)) {
                // sign changed, coerce to start scrolling and exit
                scope.launch { overscrollOffset.snapTo(0f) }
                Offset(x = 0f, y = delta.y + prevOverscrollValue)
            } else {
                scope.launch {
                    overscrollOffset.snapTo(overscrollOffset.value + delta.y)
                }
                delta.copy(x = 0f)
            }
        } else {
            Offset.Zero
        }
        val leftForScroll = delta - consumedByPreScroll
        val consumedByScroll = performScroll(leftForScroll)
        val overscrollDelta = leftForScroll - consumedByScroll
        // if it is a drag, not a fling, add the delta left to our over scroll value
        if (abs(overscrollDelta.y) > 0.5 && source == NestedScrollSource.Drag) {
            scope.launch {
                // multiply by 0.1 for the sake of parallax effect
                overscrollOffset.snapTo(overscrollOffset.value + overscrollDelta.y * 0.1f)
            }
        }
        return consumedByPreScroll + consumedByScroll
    }

    override suspend fun applyToFling(
        velocity: Velocity,
        performFling: suspend (Velocity) -> Velocity
    ) {
        val consumed = performFling(velocity)
        // when the fling happens - we just gradually animate our overscroll to 0
        val remaining = velocity - consumed
        overscrollOffset.animateTo(
            targetValue = 0f,
            initialVelocity = remaining.y,
            animationSpec = spring()
        )
    }

    override val isInProgress: Boolean
        get() = overscrollOffset.value != 0f

    // as we're building an offset modifiers, let's offset of our value we calculated
    override val effectModifier: Modifier = Modifier.offset {
        IntOffset(x = 0, y = overscrollOffset.value.roundToInt())
    }

}
