package fr.couiz.question;

import java.io.*;
import java.util.*;

public class PointsCategorieManager {
    private static final Map<CategorieQuestion, Integer> pointsParCategorie = new HashMap<>();

    public static void chargerDepuisCSV(String nomFichierRessource) throws IOException {
        InputStream in = PointsCategorieManager.class.getClassLoader().getResourceAsStream(nomFichierRessource);
        if (in == null) {
            throw new FileNotFoundException("Fichier " + nomFichierRessource + " introuvable dans les ressources.");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String ligne;
            reader.readLine(); // sauter l'en-tête
            while ((ligne = reader.readLine()) != null) {
                String[] champs = ligne.split(",");
                CategorieQuestion cat = CategorieQuestion.valueOf(champs[0].trim());
                int points = Integer.parseInt(champs[1].trim());
                pointsParCategorie.put(cat, points);
            }
        }
    }

    public static int getPoints(CategorieQuestion cat) {
        return pointsParCategorie.getOrDefault(cat, 0);
    }
}