package app.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaterialsCalculatorTest {

    // Test for calcNrOfPosts med carportLength 300 og carportWidth 300
    @Test
    public void testCalcNrOfPosts_Carport300x300() {
        int carportLength = 300;
        int carportWidth = 300;
        int expected = 4;
        int actual = MaterialsCalculator.calcNrOfPosts(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af stolper for en 300x300 carport skal være 4.");
    }

    // Test for calcNrOfPosts med carportLength 600 og carportWidth 300
    @Test
    public void testCalcNrOfPosts_Carport600x300() {
        int carportLength = 600;
        int carportWidth = 300;
        int expected = 6;
        int actual = MaterialsCalculator.calcNrOfPosts(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af stolper for en 600x300 carport skal være 4.");
    }

    // Test for calcNrOfRafters med carportLength 300 og carportWidth 300
    @Test
    public void testCalcNrOfRafters_Carport300x300() {
        int carportLength = 300;
        int carportWidth = 300;
        int expected = 6;
        int actual = MaterialsCalculator.calcNrOfRafters(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af spær for en 300x300 carport skal være 6.");
    }

    // Test for calcNrOfRafters med carportLength 600 og carportWidth 300
    @Test
    public void testCalcNrOfRafters_Carport600x300() {
        int carportLength = 600;
        int carportWidth = 300;
        int expected = 11;
        int actual = MaterialsCalculator.calcNrOfRafters(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af spær for en 600x300 carport skal være 11.");
    }

    // Test for calcNrOfBeams med carportLength 300 og carportWidth 300
    @Test
    public void testCalcNrOfBeams_Carport300x300() {
        int carportLength = 300;
        int carportWidth = 300;
        int expected = 2;
        int actual = MaterialsCalculator.calcNrOfBeams(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af remme for en 300x300 carport skal være 2.");
    }

    // Test for calcNrOfBeams med carportLength 600 og carportWidth 300
    @Test
    public void testCalcNrOfBeams_Carport600x300() {
        int carportLength = 600;
        int carportWidth = 300;
        int expected = 2;
        int actual = MaterialsCalculator.calcNrOfBeams(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af remme for en 600x300 carport skal være 2.");
    }

    // Test for calcNrOfBeams med carportLength 750 og carportWidth 300
    @Test
    public void testCalcNrOfBeams_Carport750x300() {
        int carportLength = 750;
        int carportWidth = 300;
        int expected = 3;
        int actual = MaterialsCalculator.calcNrOfBeams(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af remme for en 750x300 carport skal være 3.");
    }

    // Test for calcNrOfBeams med carportLength 780 og carportWidth 300
    @Test
    public void testCalcNrOfBeams_Carport780x300() {
        int carportLength = 780;
        int carportWidth = 300;
        int expected = 4;
        int actual = MaterialsCalculator.calcNrOfBeams(carportLength, carportWidth);
        assertEquals(expected, actual, "Antallet af remme for en 780x300 carport skal være 4.");
    }
}
