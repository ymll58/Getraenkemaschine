#pragma once

#include <Arduino.h>
#include "led.h"

const uint8_t maxNameLength = 30;

struct Bottle {
    char ingredient[maxNameLength];
    uint16_t position;
    uint8_t pumpPin;
    uint8_t ledPin;
    float mlToGram;
    uint16_t maxMl;
    uint16_t currentMl;
};

/**
 * Reads all bottles stored in the EEPROM into the internal database.
 */
void readBottlesFromEEPROM();

/**
 * Adds a bottle to the internal database and stores it in the EEPROM.
 * @param bottle The bottle to add
 */
void addBottle(Bottle bottle);

/**
 * Removes a bottle from the internal database and the EEPROM.
 * @param name The name of the bottle to remove
 */
void removeBottle(const char *name);

/**
 * Removess all bottles from the internal database and the EEPROM.
 */
void removeAllBottles();

/**
 * Returns information about a bottle by name.
 * @param name The name of the bottle
 */
Bottle getBottle(const char *name);

/**
 * Returns information about a bottle by index.
 * @param name The array index of the bottle
 */
Bottle getBottle(uint16_t index);

/**
 * @return The number of stored bottles.
 */
uint16_t getNumberOfBottles();

/**
 * Updates the current capacity of a bottle in the internal database and the EEPROM.
 * @param name The name of the bottle
 * @param ml How many ml should be removed from the current capacity
 */
void removeMl(const char *name, int16_t ml);

/**
 * Shows information about all added bottles on the serial console.
 */
void showBottles();

/**
 * Shows low capacity warnings (yellow light) on the LED rings when the current capacity is low.
 * @param percentage The percentage of the maximum capacity that is regarded as low
 */
void showLowCapacity(float percentage);

/**
 * Disables the low capacity warnings.
 */
void hideLowCapacity();

/**
 * Displays the current capacity of each bottle on the corresponding LED ring.
 * (white: OK, yellow: low capacity; red: (almost) empty)
 */
void showBottleStatus();