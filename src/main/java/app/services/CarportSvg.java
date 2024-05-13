package app.services;


public class CarportSvg
{
    private int width;
    private int length;
    private Svg carportSvg;

    public CarportSvg(int width, int height)
    {
        this.width = width;
        this.length = length;
        carportSvg = new Svg(0, 0, "-80 -20 1000 1000", "85%" );
        carportSvg.addRectangle(0,0,600, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");





        addBeams();

        //addline SKAL VÆRE EFTER ADDBEAMS, Det er en weird bug eller noget i den stil
        carportSvg.addLine(55,35,600,569.5,"stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine(600,35,55,569.5,"stroke:#000000; stroke-dasharray: 5 5;");



        addXYArrow();

        addRafterArrows();

        addText();

        addRafters();

        addColumns();
    }


    //-------------------------Vandret---------------//
    private void addBeams(){
       carportSvg.addRectangle(0,35,4.5, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0,565,4.5, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }
    private void addColumns(){

        for (double i = 0; i < 780; i+= 260)
        {
            carportSvg.addRectangle(i+110, 35.0, 9.7, 10,"stroke:#000000; fill: #ffffff" );
        }

        for (double i = 0; i < 780; i+= 260)
        {
            carportSvg.addRectangle(i+110, 562.0, 9.7, 10,"stroke:#000000; fill: #ffffff" );
        }
    }

    private void addRafters(){
        for (double i = 0; i < 780; i+= 55.714)
        {
            carportSvg.addRectangle(i, 0.0, 600, 4.5,"stroke:#000000; fill: #ffffff" );
        }
    }

    //--------------------******Test metode******-----------//
    private void addLine(){

        //---------------------------------------Y akse----------------------//
        carportSvg.addLine(-45,650,-45,0,"stroke:#000000");

        //---------------------------------------X akse----------------------//
        carportSvg.addLine(-15,650,780,650,"stroke:#000000");

    }
    //-----------------------------------------------------//


    private void addXYArrow(){
        carportSvg.addArrow(-45,650,-45,0,"stroke:#000000");
        carportSvg.addArrow(-15,650,780,650,"stroke:#000000");

    }
    private void addText(){
        carportSvg.addText(-60,325,-90,"6000");
        carportSvg.addText(390,700,0,"6000");


    }
    private void addRafterArrows(){

        //---TODO Debugging arrows
        for (double i = 0; i < 780; i+= 55.714)
        {
            carportSvg.addArrow(i, 0.0, i, 4.5,"stroke:#000000; fill: #ffffff" );
        }
    }

    @Override
    public String toString()
    {
        return carportSvg.toString();
    }
}
