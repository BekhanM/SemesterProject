package app.persistence;

import app.entities.Materials;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {

    //Henter al materiale vi har i databasen.
    public static List<Materials> getMaterials(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM materials";
        List<Materials> materials = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                int materialID = rs.getInt("materialID");
                String name = rs.getString("name");
                int pricePrMeter = rs.getInt("priceprmeter");
                int length = rs.getInt("length");

                materials.add(new Materials(materialID, name, pricePrMeter, length));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching materials", e.getMessage());
        }

        return materials;
    }

    // Finder den korrekte længde af materiale, baseret på carport længde og navn af materiale.
    public static List<Materials> selectMaterials(int carportLength, String name, ConnectionPool connectionPool) throws DatabaseException {
        // Gemmer alle materials i en liste af materials og kalder den ... materials
        List<Materials> materials = getMaterials(connectionPool);

        // Når vi har fundet det rette materiale til jobbet ift. carport længde, så har vi denne bad boy klar.
        List<Materials> selectedMaterials = new ArrayList<>();

        // Kører materialle listen igennem og finder den rette ift. navn og længde.
        // Den rette længde bliver fundet vha. carport. Enten større eller lig med.
        // Herefter er det først til mølle fra listen.
        for (Materials material : materials) {
            if (material.getName().equals(name) && material.getLength() >= carportLength) {
                selectedMaterials.add(material);
                return selectedMaterials;
            }
        }

        // Hvis vi ikke kunne finde noget materiale. Vil det sige at carporten er over 600 cm.
        // Derfor tager vi bare det længste materiale vi har.
        if (carportLength > 600) {
            // Loopen kører så længe materialet er over 0. Det sådan den tager fat i det længste materiale der er.
            // Lidt sølle I know, men det var det eneste der virkede.
            int longestLength = 0;
            Materials longestMaterial = null;
            for (Materials material : materials) {
                if (material.getName().equals(name) && material.getLength() > longestLength) {
                    longestLength = material.getLength();
                    longestMaterial = material;
                }
            }
            if (longestMaterial != null) {
                selectedMaterials.add(longestMaterial);
                return selectedMaterials;
            }
        }

        if (selectedMaterials.isEmpty()) {
            throw new DatabaseException("No suitable material length found");
        }
        return selectedMaterials;
    }
}
