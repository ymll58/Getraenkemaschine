#include "bottles.h"

#include <Vector.h>
#include <EEPROM.h>

const uint16_t maxBottles = 20;
Bottle storageArray[maxBottles];
Vector<Bottle> bottles(storageArray);

const uint16_t invalidIndex = 65535; // index will never be reached normally

bool lowCapacityEnabled = false;
float lowCapacityPercentage = 0.2;

void readBottlesFromEEPROM() {
    // Read vector from EEPROM
    uint16_t size;
    EEPROM.get(0, size);
    uint16_t maxSize;
    EEPROM.get(2, maxSize);
    EEPROM.get(4, storageArray);
    bottles.setStorage(storageArray, maxSize, size);
}

uint16_t getIndexByName(const char *name) {
    for (uint16_t i = 0; i < bottles.size(); i++) {
        if (strcmp(bottles[i].ingredient, name) == 0) {
            return i;
        }
    }
    return invalidIndex; // name not found
}

void clearEEPROM() {
    for (uint16_t i = 0 ; i < EEPROM.length(); i++) {
        EEPROM.update(i, 0);
    }
}

void writeDataToEEPROM() {
    // Remove previous data
    clearEEPROM();

    // Write new data
    EEPROM.put(0, bottles.size());
    EEPROM.put(2, bottles.max_size());
    EEPROM.put(4, storageArray);
}

void addBottle(Bottle bottle) {
    if (getIndexByName(bottle.ingredient) == invalidIndex) {
        // Bottle is not present yet, so add it
        bottles.push_back(bottle);
        writeDataToEEPROM();
    }
}

void removeBottle(const char *name) {
    uint16_t bottleIndex = getIndexByName(name);
    bottles.remove(bottleIndex);
    writeDataToEEPROM();
}

void removeAllBottles() {
    bottles.clear();
    writeDataToEEPROM();
}

Bottle getBottle(const char *name) {
    uint16_t index = getIndexByName(name);
    if (index == invalidIndex) {
        Bottle invalid;
        strcpy(invalid.ingredient, "Invalid");
        invalid.position = 0;
        invalid.pumpPin = 0;
        invalid.ledPin = 0;
        invalid.mlToGram = 0;
        invalid.maxMl = 0;
        invalid.currentMl = 0;
        return invalid;
    } else {
        return bottles[index];
    }
}

Bottle getBottle(uint16_t index) {
    return bottles[index];
}

uint16_t getNumberOfBottles() {
    return bottles.size();
}

void removeMl(const char *name, int16_t ml) {
    uint16_t index = getIndexByName(name);
    bottles[index].currentMl -= ml;
    writeDataToEEPROM();

    showBottleStatus();
}

void showBottles() {
    Serial.println("name,position,pump pin,led pin,ml to gram,max capacity,current capacity");
    for (uint16_t i = 0; i < bottles.size(); i++) {
        Serial.print(bottles[i].ingredient);
        Serial.print(",");
        Serial.print(bottles[i].position);
        Serial.print(",");
        Serial.print(bottles[i].pumpPin);
        Serial.print(",");
        Serial.print(bottles[i].ledPin);
        Serial.print(",");
        Serial.print(bottles[i].mlToGram);
        Serial.print(",");
        Serial.print(bottles[i].maxMl);
        Serial.print(",");
        Serial.println(bottles[i].currentMl);
    }
}

void showLowCapacity(float percentage) {
    lowCapacityEnabled = true;
    lowCapacityPercentage = percentage;
    showBottleStatus();
}

void hideLowCapacity() {
    lowCapacityEnabled = false;
    showBottleStatus();
}

void showBottleStatus() {
    for (uint16_t i = 0; i < bottles.size(); i++) {
        float currentMl = bottles[i].currentMl;
        float maxMl = bottles[i].maxMl;

        if (currentMl <= 30) {
            // Bottle is (almost) empty
            showLEDPattern(bottles[i].ledPin, LEDPattern::CONSTANT_RED);
        } else if (lowCapacityEnabled && currentMl <= (lowCapacityPercentage * maxMl)) {
            // Capacity is low
            showLEDPattern(bottles[i].ledPin, LEDPattern::CONSTANT_YELLOW);
        } else {
            showLEDPattern(bottles[i].ledPin, LEDPattern::CONSTANT_WHITE);
        }
    }
}