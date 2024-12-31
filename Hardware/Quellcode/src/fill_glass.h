#pragma once

#include <Arduino.h>
#include <LiquidCrystal_I2C.h>

#include "status_light.h"
#include "weight_sensor.h"
#include "led.h"
#include "data.h"

enum class FillState {
    FILLING, GLASS_REMOVED, BOTTLE_EMPTY
};

/**
 * Configures the pins for the pumps, the magnet and the button.
 * Needs to be called before filling a glass.
 */
void setupFillGlass();

/**
 * Waits until the weight sensor detects a glass of a specific type.
 * @param lcd The display to print status information on
 * @param order The order that should be processed afterwards. Contains the information about the required glass type
 */
void waitForGlass(LiquidCrystal_I2C &lcd, const Order &order);

/**
 * Fills an ingredient into the glass.
 * 
 * @param lcd The display to print status information on
 * @param pumpPin The pin of the pump that is connected to the ingredient
 * @param name The name of the ingredient to be filled in
 * @param gramsToFill The total weight that should be reached
 */
void fillGlass(LiquidCrystal_I2C &lcd, uint8_t pumpPin, String name, int32_t gramsToFill);

/**
 * Waits until the weight sensor no longer detects a glass.
 * 
 * @param lcd The display to print status information on
 * @param order The order that was processed. Contains the information about the glass type
 */
void waitUntilGlassIsRemoved(LiquidCrystal_I2C &lcd, const Order &order);