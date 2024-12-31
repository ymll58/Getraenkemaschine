#pragma once

enum class LEDPattern {
    ERROR_RED, FADING_WHITE, CONSTANT_WHITE, CONSTANT_YELLOW, CONSTANT_RED
};

/**
 * Shows a pattern on the LED ring.
 * @param ledPin The pin that the LED ring is connected to
 * @param pattern The pattern to show
 */
void showLEDPattern(uint8_t ledPin, LEDPattern pattern);