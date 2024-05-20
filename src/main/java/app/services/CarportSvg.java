package app.services;


import java.text.DecimalFormat;

public class CarportSvg
{

    private Svg carportSvg;

    //----*** VARIABLES ***----//
    private double carportWidth;
    private double carportLength;
    private  double colomnSpace = 310;
    private  double columnStartVal= 110;
    private  double rafterSpace = 55;
    private String textX="";
    private String textY= "";



    //TODO Det er nogle variabler som har fast værdi, men skal nok skiftes ud med hensyn til regnestykke

    //-------------------------//

    public CarportSvg(double carportWidth, double carportLength)
    {
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        carportSvg = new Svg(0, 0, "-80 -50 1000 1000", "85%" );
        carportSvg.addRectangle(0,0, carportWidth, carportLength, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        textX=String.valueOf(carportLength);
        textY= String.valueOf(carportWidth);


        addBeams(carportLength);
        //addline SKAL VÆRE EFTER ADDBEAMS, Det er en weird bug
        addCross(carportWidth, carportLength /*,crossLineX1,crossLineY1,crossLineY2*/);

        addXYArrow(carportWidth, carportLength);

        addRafterArrows(carportLength,rafterSpace);

        addText(carportLength, carportWidth,textX,textY);

        addRafters(carportLength, carportWidth,rafterSpace);

        addColumns(carportLength,colomnSpace,columnStartVal);

    }






    //-------------------------Vatnrætt-----------------------------//
    private void addBeams(double carportWidth){
        int spaceControl = 35;
        double bottomStartSpace = this.carportWidth -spaceControl;


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
            double remainingLength = carportWidth-colomnSpace;

            //Min post
            carportSvg.addRectangle(0 , 35.0, 9.7, 10, "stroke:#000000; fill: #ffffff");
            carportSvg.addRectangle(carportWidth-10 , 35.0, 9.7, 10, "stroke:#000000; fill: #ffffff");
            carportSvg.addRectangle(0 , this.carportWidth - 39, 9.7, 10, "stroke:#000000; fill: #ffffff");
            carportSvg.addRectangle(carportWidth-10 , this.carportWidth - 39, 9.7, 10, "stroke:#000000; fill: #ffffff");

        while(remainingLength>0){


            carportSvg.addRectangle(colomnSpace, 35.0, 9.7, 10, "stroke:#000000; fill: #ffffff");

            carportSvg.addRectangle(colomnSpace, this.carportWidth - 39, 9.7, 10, "stroke:#000000; fill: #ffffff");

            remainingLength-=colomnSpace;

            colomnSpace+=colomnSpace;
        }
    }




    private void addXYArrow(double carportLength, double carportWidth){

        carportSvg.addArrow(-45,carportLength,-45,0,"stroke:#000000");

        carportSvg.addArrow(0,carportLength+50,carportWidth,carportLength+50,"stroke:#000000");

    }



    private void addCross(double carportLength,double carportWidth){
        double X1 = carportLength-55;
        double Y1= carportWidth-35;

        double crossLineX1 = carportLength-X1;
        double crossLineY1= carportWidth-Y1;
        double crossLineY2= carportLength-30.5;
        double crossLineX2= carportWidth-30.5;


        carportSvg.addLine(crossLineX1,crossLineY1,crossLineX2,crossLineY2,"stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine(crossLineX2,crossLineY1,crossLineX1,crossLineY2,"stroke:#000000; stroke-dasharray: 5 5;");

        System.out.println(crossLineY2);

    }
    private void addText(double x, double y, String textX,String textY){

        carportSvg.addText(-60,y/2,-90, textY);
        carportSvg.addText(x/2, carportWidth +75,0,textX);


    }
    private void addRafterArrows(double carportWidth, double rafterSpace) {
        DecimalFormat df = new DecimalFormat("#.#");
        String formatNr = df.format(rafterSpace);
        double rafterDiscard = 0;
        double iAmount = 0;
        for (double i = 2.25; i <= carportWidth-55; i += rafterSpace) {
            carportSvg.addLine(i,-10,i,10,"stroke:#000000");
            carportSvg.addArrow(i, -10, i + rafterSpace, -10, "stroke:#000000");
            carportSvg.addText(i + rafterSpace / 2, -15, 0, formatNr);
            rafterDiscard+=rafterSpace;
            iAmount = i;

        }
        rafterDiscard = carportWidth-rafterDiscard;
        iAmount = carportWidth-rafterDiscard;
        carportSvg.addLine(iAmount+2.25,-10,iAmount+2.25,10,"stroke:#000000");
    }

    @Override
    public String toString()
    {
        return carportSvg.toString();
    }
}
