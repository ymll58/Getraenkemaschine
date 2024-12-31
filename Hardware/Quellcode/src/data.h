#pragma once

#include <Arduino.h>

#include "bottles.h"

struct Ingredient {
    String name;
    int16_t ml;
};

struct Order {
    int16_t numIngredients;
    char glassType;
    Ingredient *ingredients;
};

/**
 * Reads a new order from the serial interface in the format:
 * "Order,Number of ingredients,Glass type,Name of first ingredient,Ml of first ingredient,..."
 * @return The order that was received
 */
Order readOrderFromSerial();

/**
 * Prints information about an order to the serial console.
 * @param order The order to print information about
 */
void printOrder(const Order &order);

/**
 * Frees the memory of the ingredients array.
 * Needs to be called after processing an order to avoid memory leaks.
 * @param order The struct that is no longer needed
 */
void freeOrder(Order &order);

/**
 * @return true if the current capacites of all needed bottles are high enough.
 */
bool isOrderValid(const Order &order);

/**
 * @return The ingredient that is missing or whose capacity is insufficient (call isOrderValid() first).
 */
String getMissingIngredient(const Order &order);

/**
 * Returns the weight of a specific type of glass.
 * @return the weight of the glass (type A: 425 g, type B: 286 g, type C: 147 g)
 */
int32_t getGlassWeight(char glassType);

/**
 * Converts mililiters to grams according to the type of the ingredient.
 * @param ingredient The ingredient that should be converted
 * @param bottle The bottle that contains the ingredient
 * @return The amount of grams that need to be filled in
 */
int32_t convertMlToGram(const Ingredient &ingredient, const Bottle &bottle);

/**
 * Reads a new bottle from the serial interface in the format:
 * "AddBottle,Bottle name,Bottle position (cm),Pump pin,LED pin,Factor to convert from ml to gram,Max capacity (ml)"
 * @return The bottle that was read in
 */
Bottle readBottleFromSerial();