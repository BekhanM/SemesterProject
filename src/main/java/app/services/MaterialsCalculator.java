package app.services;

public class MaterialsCalculator {
    private static final int maxSpaceBetweenPosts = 310;
    private static final int minimumAmountOfPostsNeededToHaveACarportObviously = 4;
    private static final int maxSpaceBetweenRafters = 55;
    private static final int minimumAmountOfBeamsNeededToHaveACarportObviously = 2;

    // Udregner antallet af posts baseret på carports - længde og bredde.
    public static int calcNrOfPosts(int carportLength, int carportWidth) {
        int minNrOfPosts = minimumAmountOfPostsNeededToHaveACarportObviously;
        //Der skal være et post pr. 310 cm. Restlængde er et hjælpe objekt til dette.
        int remainingLength = carportLength - maxSpaceBetweenPosts;

        //Så længe restlængde er over 0, tilføjer vi to stolper (en på hver side).
        while (remainingLength > 0) {
            minNrOfPosts += 2;
            remainingLength -= maxSpaceBetweenPosts;
        }
        return minNrOfPosts;
    }

    // Udregner antallet af spær baseret på carports - længde og bredde.
    public static int calcNrOfRafters(int carportLength, int carportWidth) {
        int amountOfRafters = 0;

        int remainingLength = carportLength - maxSpaceBetweenRafters;

        // Der tilføjes et spær pr 55 cm.
        while (remainingLength > 0) {
            amountOfRafters += 1;
            remainingLength -= maxSpaceBetweenRafters;
        }
        return amountOfRafters;
    }

    // Udregner antallet af remme baseret på carports - længde og bredde
    public static int calcNrOfBeams(int carportLength, int carportWidth) {
        // Vi starter altid med to remme - obviously
        int minNrOfBeams = minimumAmountOfBeamsNeededToHaveACarportObviously;

        //Ekstra remme tilføjes hvis carporten overstiger maksimum-remme-materiale-længde
        if (carportLength > 600 && carportLength <= 750) {
            minNrOfBeams += 1;
        } else if (carportLength > 750) {
            minNrOfBeams += 2;
        }
        return minNrOfBeams;
    }
}
