package app.services;


import java.text.DecimalFormat;

public class CarportSvg
{
    private int width;
    private int length;
    private Svg carportSvg;

    //----*** VARIABLES ***----//
    private double carportLength;
    private double carportWidth;
    private final double colomnSpace = 260;
    private final double columnStartVal= 110;
    private final double rafterSpace = 55.714;


    private final double crossLineX1 = 55;
    private final double crossLineY1 = 35;
    private final double crossLineY2 = 569.5;


    //TODO Det er nogle variabler som har fast værdi, men skal nok skiftes ud med hensyn til regnestykke

    //-------------------------//

    public CarportSvg( double carportLength,double carportWidth)
    {
        this.carportLength = carportLength;
        this.carportWidth = carportWidth;
        carportSvg = new Svg(0, 0, "-80 -50 1000 1000", "85%" );
        carportSvg.addRectangle(0,0,carportLength, carportWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");



        addBeams();
        //addline SKAL VÆRE EFTER ADDBEAMS, Det er en weird bug
        addCross();

        addXYArrow();

        addRafterArrows();

        addText();

        addRafters();

        addColumns();

    }






    //-------------------------Vandrætt-----------------------------//
    private void addBeams(){

       carportSvg.addRectangle(0,35,4.5, carportWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0,565,4.5, carportWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");

    }
    //------------------------------------------------------------//



    //-------------------------Lodrætt-----------------------------//
    private void addRafters(){
        for (double i = 0; i < carportWidth; i+= rafterSpace)
        {
            carportSvg.addRectangle(i, 0.0, carportLength, 4.5,"stroke:#000000; fill: #ffffff" );
        }
    }
    //------------------------------------------------------------//

    private void addColumns(){

        for (double i = 0; i < carportWidth; i+= colomnSpace)
        {
            carportSvg.addRectangle(i+columnStartVal, 35.0, 9.7, 10,"stroke:#000000; fill: #ffffff" );
        }

        for (double i = 0; i < carportWidth; i+= colomnSpace)
        {
            carportSvg.addRectangle(i+columnStartVal, 562.0, 9.7, 10,"stroke:#000000; fill: #ffffff" );
        }
    }




    private void addXYArrow(){

        carportSvg.addArrow(-45,carportLength,-45,0,"stroke:#000000");

        carportSvg.addArrow(0,carportLength+50,carportWidth,650,"stroke:#000000");

    }



    private void addCross(){
        carportSvg.addLine(crossLineX1,crossLineY1,carportLength,crossLineY2,"stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine(carportLength,crossLineY1,crossLineX1,crossLineY2,"stroke:#000000; stroke-dasharray: 5 5;");


        /*carportSvg.addLine(55,35,carportLength,569.5,"stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine(carportLength,35,55,569.5,"stroke:#000000; stroke-dasharray: 5 5;");*/

    }
    private void addText(){


        carportSvg.addText(-60,carportLength/2,-90, String.valueOf(carportLength));


        carportSvg.addText(carportWidth/2, 700,0,String.valueOf(carportWidth));


    }
    private void addRafterArrows(){
        DecimalFormat df = new DecimalFormat("#,#");
        String formatNr = df.format(rafterSpace);
        double lengthTracker = 0;
        //---TODO Debugging arrows
        for (double i = 0; i < carportWidth; i+= rafterSpace)
        {
            carportSvg.addArrow(i, -10, i, -10,"stroke:#000000" );

            carportSvg.addText(i+rafterSpace/2,-15,0,formatNr);

            lengthTracker=i;


        }
        carportSvg.addLine(0,-10,lengthTracker,-10,"stroke:#000000");
    }

    @Override
    public String toString()
    {
        return carportSvg.toString();
    }
}
