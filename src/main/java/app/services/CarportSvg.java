package app.services;


import java.text.DecimalFormat;

public class CarportSvg
{

    private Svg carportSvg;

    //----*** VARIABLES ***----//
    private double carportLength = 600;
    private double carportWidth = 780;
    private  double colomnSpace = 260;
    private  double columnStartVal= 110;
    private  double rafterSpace = 55.714;
    private String textX=String.valueOf(carportWidth);
    private String textY= String.valueOf(carportLength);

   // private  double crossLineX1 = 55;
   // private  double crossLineY1 = 35;
   // private  double crossLineY2 = 569.5;


    //TODO Det er nogle variabler som har fast værdi, men skal nok skiftes ud med hensyn til regnestykke

    //-------------------------//

    public CarportSvg( double carportLength,double carportWidth)
    {
        this.carportLength = carportLength;
        this.carportWidth = carportWidth;
        carportSvg = new Svg(0, 0, "-80 -50 1000 1000", "85%" );
        carportSvg.addRectangle(0,0,carportLength, carportWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");



        addBeams(carportWidth);
        //addline SKAL VÆRE EFTER ADDBEAMS, Det er en weird bug
        addCross(carportLength/*,crossLineX1,crossLineY1,crossLineY2*/);

        addXYArrow(carportLength,carportWidth);

        addRafterArrows(carportWidth,rafterSpace);

        addText(carportWidth,carportLength,textX,textY);

        addRafters(carportWidth,carportLength,rafterSpace);

        addColumns(carportWidth,colomnSpace,columnStartVal);

    }






    //-------------------------Vandrætt-----------------------------//
    private void addBeams(double carportWidth){
        int spaceControl = 35;
        double bottomStartSpace = carportLength-spaceControl;


       carportSvg.addRectangle(0,spaceControl,4.5, carportWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0,bottomStartSpace,4.5, carportWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");

    }
    //------------------------------------------------------------//



    //-------------------------Lodrætt-----------------------------//
    private void addRafters(double carportWidth,double carportLength, double rafterSpace){
        for (double i = 0; i < carportWidth; i+= rafterSpace)
        {
            carportSvg.addRectangle(i, 0.0, carportLength, 4.5,"stroke:#000000; fill: #ffffff" );
        }
    }
    //------------------------------------------------------------//

    private void addColumns(double carportWidth, double colomnSpace, double columnStartVal){

        for (double i = 0; i < carportWidth; i+= colomnSpace)
        {
            carportSvg.addRectangle(i+columnStartVal, 35.0, 9.7, 10,"stroke:#000000; fill: #ffffff" );
        }

        for (double i = 0; i < carportWidth; i+= colomnSpace)
        {
            carportSvg.addRectangle(i+columnStartVal, 562.0, 9.7, 10,"stroke:#000000; fill: #ffffff" );
        }
    }




    private void addXYArrow(double carportLength, double carportWidth){

        carportSvg.addArrow(-45,carportLength,-45,0,"stroke:#000000");

        carportSvg.addArrow(0,carportLength+50,carportWidth,carportLength+50,"stroke:#000000");

    }



    private void addCross(double carportLength/*, double crosslineX1, double crossLineY1, double crossLineY2*/){
        double X1 = carportLength-55;
        double Y1= carportWidth-35;

        double crossLineX1 = carportLength-X1;
        double crossLineY1= carportWidth-Y1;
        double crossLineY2= carportLength-30.5;


        carportSvg.addLine(crossLineX1,crossLineY1,carportLength,crossLineY2,"stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine(carportLength,crossLineY1,crossLineX1,crossLineY2,"stroke:#000000; stroke-dasharray: 5 5;");


        /*carportSvg.addLine(55,35,carportLength,569.5,"stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine(carportLength,35,55,569.5,"stroke:#000000; stroke-dasharray: 5 5;");*/

    }
    private void addText(double x, double y, String textX,String textY){

        carportSvg.addText(-60,y/2,-90, textY);
        carportSvg.addText(x/2, carportLength+75,0,textX);


    }
    private void addRafterArrows(double carportWidth, double rafterSpace) {
        DecimalFormat df = new DecimalFormat("#.#");
        String formatNr = df.format(rafterSpace);
        for (double i = 2.25; i <= carportWidth; i += rafterSpace) {
            carportSvg.addLine(i,-10,i,10,"stroke:#000000");
            carportSvg.addArrow(i, -10, i + rafterSpace, -10, "stroke:#000000");
            carportSvg.addText(i + rafterSpace / 2, -15, 0, formatNr);
        }
        carportSvg.addLine(carportWidth+2.25,-10,carportWidth+2.25,10,"stroke:#000000");
    }

    @Override
    public String toString()
    {
        return carportSvg.toString();
    }
}
