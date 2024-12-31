#include "data.h"

Order readOrderFromSerial() {
    int16_t numIngredients = Serial.readStringUntil(',').toInt();
    char glassType = Serial.readStringUntil(',').charAt(0);

    // Must be allocated on the heap, otherwise the memory of the array would be freed when exiting the function
    Ingredient *ingredients = new Ingredient[numIngredients];

    for (int16_t i = 0; i < numIngredients; i++) {
        String name = Serial.readStringUntil(',');
        ingredients[i].name = name;
        int16_t ml = Serial.readStringUntil(',').toInt();
        ingredients[i].ml = ml;
    }

    Order order = { numIngredients, glassType, ingredients };

    return order;
}

void printOrder(const Order &order) {
    Serial.print(order.numIngredients);
    Serial.println(" ingredients:");

    for (int16_t i = 0; i < order.numIngredients; i++) {
        Serial.print(order.ingredients[i].name);
        Serial.print(", ");
        Serial.print(order.ingredients[i].ml);
        Serial.println(" ml");
    }
}

void freeOrder(Order &order) {
    delete[] order.ingredients;
    order.ingredients = nullptr;
}

bool isOrderValid(const Order &order) {
    for (int16_t i = 0; i < order.numIngredients; i++) {
        int16_t currentMl = getBottle(order.ingredients[i].name.c_str()).currentMl;
        // 20 ml: currentMl may not be completely accurate
        if (currentMl < order.ingredients[i].ml + 20) {
            // The current capacity is too low to fullfil the order
            return false;
        }
    }

    return true;
}

String getMissingIngredient(const Order &order) {
    for (int16_t i = 0; i < order.numIngredients; i++) {
        int16_t currentMl = getBottle(order.ingredients[i].name.c_str()).currentMl;
        // 20 ml: currentMl may not be completely accurate
        if (currentMl < order.ingredients[i].ml + 20) {
            // The current capacity is too low to fullfil the order
            return order.ingredients[i].name;
        }
    }

    return "None";
}

int32_t getGlassWeight(char glassType) {
    if (glassType == 'A') {
        return 425;
    } else if (glassType == 'B') {
        return 286;
    } else if (glassType == 'C') {
        return 147;
    } else {
        return 200; // default weight
    }
}

int32_t convertMlToGram(const Ingredient &ingredient, const Bottle &bottle) {
    return ingredient.ml * bottle.mlToGram;
}

Bottle readBottleFromSerial() {
    Bottle bottle;

    String name = Serial.readStringUntil(',');
    uint16_t position = Serial.readStringUntil(',').toInt();
    uint8_t pumpPin = Serial.readStringUntil(',').toInt();
    uint8_t ledPin = Serial.readStringUntil(',').toInt();
    float mlToGram = Serial.readStringUntil(',').toFloat();
    uint16_t maxMl = Serial.readStringUntil(',').toInt();

    strcpy(bottle.ingredient, name.c_str());
    bottle.position = position;
    bottle.pumpPin = pumpPin;
    bottle.ledPin = ledPin;
    bottle.mlToGram = mlToGram;
    bottle.maxMl = maxMl;
    bottle.currentMl = bottle.maxMl;

    return bottle;
}