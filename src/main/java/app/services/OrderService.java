package app.services;

import app.entities.Orders;
import app.entities.Materials;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.persistence.OrderlineMapper;

import java.util.List;

// Skabt for at gøre således at MaterialCalculator kun fokuserer på udregningerne.
// Behandler en ordre ved at oprette den,  beregne nødvendig materiale, og tilføj til DB.
public class OrderService {
    private OrderMapper orderMapper;
    private OrderlineMapper orderlineMapper;
    private MaterialMapper materialMapper;
    private ConnectionPool connectionPool;

    public OrderService(OrderMapper orderMapper, OrderlineMapper orderlineMapper, MaterialMapper materialMapper, ConnectionPool connectionPool) {
        this.orderMapper = orderMapper;
        this.orderlineMapper = orderlineMapper;
        this.materialMapper = materialMapper;
        this.connectionPool = connectionPool;
    }


    public void processOrder(int userID, int carportLength, int carportWidth) throws DatabaseException {
        // Udregner antallet af materialer ift. carports - længde og bredde
        int posts = MaterialsCalculator.calcNrOfPosts(carportLength, carportWidth);
        int rafters = MaterialsCalculator.calcNrOfRafters(carportLength, carportWidth);
        int beams = MaterialsCalculator.calcNrOfBeams(carportLength, carportWidth);

        // Finder det rette længde af de forskellige materialer.
        List<Materials> postMaterials = materialMapper.selectMaterials(300, "97x97 mm. trykimp. Stolpe", connectionPool);
        List<Materials> beamMaterials = materialMapper.selectMaterials(carportLength, "45x195 mm. sprærtræ ubh.", connectionPool);
        List<Materials> rafterMaterials = materialMapper.selectMaterials(carportWidth, "45x195 mm. sprærtræ ubh.", connectionPool);

        // Udregner prisen baseret på antallet af materialet, og længden af materiallet.
        int totalPrice = calculateTotalPrice(posts, postMaterials) + calculateTotalPrice(beams, beamMaterials) + calculateTotalPrice(rafters, rafterMaterials);

        // Skaber et order objekt, som bruges primært til at oprette et orderID.
        // Vi bruger setters (thank God for them) til at sætte carports - længde og bredde.
        Orders order = new Orders(0, userID, totalPrice);
        order.setCarportLength(carportLength);
        order.setCarportWidth(carportWidth);
        order.setRoofTiles(false);

        // Skaber et orderID int og tildeler den værdien som bliver created i createOrderID metoden
        int orderID = orderMapper.createOrderID(order, connectionPool);

        //Herefter kalder vi på addOrderLinesMetoden, som samler al information vi har udregnet og indsætter dem i DB.
        addOrderlines(orderID, postMaterials, posts);
        addOrderlines(orderID, beamMaterials, 2);
        addOrderlines(orderID, rafterMaterials, rafters);

        // If statements baseret på carportlængden.
        // I vores system er der brug for ekstra remme materiale hvis carporten overstiger max. remmematerialelængde,
        // Denne metoder tager højde for det, ved at tilføje dem til databasen.
        if (carportLength > 600 && carportLength <= 750) {
            orderlineMapper.addOrderline(orderID, 3, 1, connectionPool);
        } else if (carportLength > 750) {
            orderlineMapper.addOrderline(orderID, 3, 2, connectionPool);
        }
    }

    // Udregner total materiale pris, baseret på antal og længde af materiale.
    private int calculateTotalPrice(int amount, List<Materials> materials) {
        int totalPrice = 0;

        // Kører materiale listen igennem i et for-each loop.
        // Vi finder længden pr. materiale i listen og tager imod antallet, og ganger det med pris pr. m.
        for (Materials material : materials) {
            int materialLengthMeters = material.getLength() / 100; // For at konverterer cm. til m.
            totalPrice += material.getPricePrMeter() * materialLengthMeters * amount;
        }
        return totalPrice;
    }

    // Bruger orderlinemapper metoden til at indsætte i DB. Der bliver skabt en ny orderline pr. ny materiale.
    // OrderID holder dem samlet.
    private void addOrderlines(int orderID, List<Materials> materials, int quantity) throws DatabaseException {
        for (Materials material : materials) {
            orderlineMapper.addOrderline(orderID, material.getMaterialID(), quantity, connectionPool);
        }
    }
}
