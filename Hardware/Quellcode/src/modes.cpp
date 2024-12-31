#include "modes.h"

uint32_t lastChange = 0;
uint16_t bottleIndex = 0;

void processOrder(LiquidCrystal_I2C &lcd, const Order &order) {
    waitForGlass(lcd, order);

    int32_t totalWeight = 0;
    for (int16_t i = 0; i < order.numIngredients; i++) {
        // Move the platform to the correct position
        Bottle bottle = getBottle(order.ingredients[i].name.c_str());
        movePlatformTo(bottle.position);

        // Wait to ensure that the platform is stationary
        delay(1000);

        totalWeight += convertMlToGram(order.ingredients[i], bottle);
        fillGlass(lcd, bottle.pumpPin, order.ingredients[i].name, totalWeight);

        // Update current capacity of the bottle
        removeMl(order.ingredients[i].name.c_str(), order.ingredients[i].ml);

        // Wait to ensure that the filling of the glass is completed
        delay(1000);
    }

    // Move back to the starting position
    movePlatformTo(0);

    waitUntilGlassIsRemoved(lcd, order);
}

void operatingMode(LiquidCrystal_I2C &lcd) {
    lcd.setCursor(0, 0);
    lcd.print("Operating mode");
    lcd.setCursor(0, 1);
    lcd.print("Ready");

    // Wait for data from serial input
    if (Serial.available()) {
        String type = Serial.readStringUntil(',');
        if (type.equals("Order")) {
            Order order = readOrderFromSerial();

            // Print order to serial for debugging
            printOrder(order);

            // Check if the order can be processed with the current capacities
            if (isOrderValid(order)) {
                // Process the order
                processOrder(lcd, order);
            } else {
                // Show error
                String missingIngredient = getMissingIngredient(order);
                lcd.clear();
                lcd.setCursor(0, 0);
                lcd.print("Re-fill/add");
                lcd.setCursor(0, 1);
                lcd.print(missingIngredient);
                delay(4000);
                lcd.clear();
            }

            // Free memory
            freeOrder(order);
        } else if (type.equals("AddBottle")) {
            Bottle bottle = readBottleFromSerial();
            addBottle(bottle);
            Serial.println("Done");
        } else if (type.equals("RemoveBottle")) {
            String name = Serial.readStringUntil(',');
            removeBottle(name.c_str());
            Serial.println("Done");
        } else if (type.equals("Reset")) {
            removeAllBottles();
            Serial.println("Done");
        } else if (type.equals("ShowBottles")) {
            showBottles();
        } else if (type.equals("ShowLowCapacity")) {
            float percentage = Serial.readStringUntil(',').toFloat();
            showLowCapacity(percentage);
            Serial.println("Done");
        } else if (type.equals("HideLowCapacity")) {
            hideLowCapacity();
            Serial.println("Done");
        }
    }
}

void bottleStatusMode(LiquidCrystal_I2C &lcd) {
    Bottle bottle = getBottle(bottleIndex);

    if ((millis() - lastChange) >= 2000) {
        lcd.clear();
        lcd.setCursor(0, 0);
        lcd.print("ST: Current ml");

        if (getNumberOfBottles() != 0) {
            // Show saved bottles
            lcd.setCursor(0, 1);
            lcd.print(bottle.ingredient);
            lcd.print(": ");
            lcd.print(bottle.currentMl);
            lcd.print(" ml");

            // Change displayed bottle every two seconds
            if (bottleIndex < getNumberOfBottles() - 1) {
                bottleIndex++;
            } else {
                bottleIndex = 0;
            }
        } else {
            // Show that there are no bottles
            lcd.setCursor(0, 1);
            lcd.print("Empty");
        }

        lastChange = millis();
    }
}

void weighingMode(LiquidCrystal_I2C &lcd) {
    if ((millis() - lastChange) >= 1000) {
        // Update weight once per second
        int32_t weight = measureWeight();
        lcd.clear();
        lcd.setCursor(0, 0);
        lcd.print("ST: Glass weight");
        lcd.setCursor(0, 1);
        lcd.print(weight);
        lcd.print(" g");

        lastChange = millis();
    }
}