package com.pluralsight;

import com.pluralsight.model.Car;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MainTest   {
    @Test
void carOverloadedConstructor_setsSoldFalse() {
    Car c = new Car("VIN1","Toyota","Corolla",2024,19999.0);
    assertFalse(c.isSold());
}

@Test
void hashmap_basicPutGet() {
    HashMap<String, Car> inv = new HashMap<>();
    Car c = new Car("VIN2","Honda","Civic",2020,15000.0,false);
    inv.put(c.getVin(), c);
    assertEquals("Honda", inv.get("VIN2").getMake());
}

@Test
void hasAddCarOverload() throws Exception {
    Method m = Main.class.getDeclaredMethod("addCar", String.class,String.class,String.class,int.class,double.class);
    assertNotNull(m);
}
}