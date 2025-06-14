package fr.couiz.question;

import java.io.*;
import java.util.*;

public class CsvLecteur {

    public static Question creerQuestion(String[] champs) {
        Type type = Type.valueOf(champs[0].trim());
        CategorieQuestion categorie = CategorieQuestion.valueOf(champs[1].trim());
        String enonce = champs[2].trim();

        if (type == Type.QMC) {
            List<String> choix = Arrays.asList(champs[3].split(";"));
            int bonneReponse = Integer.parseInt(champs[4].trim());
            return new QuestionQCM(enonce, categorie, choix, bonneReponse);
        } else if (type == Type.QPR) {
            String reponse = champs[6].trim(); // colonne 7
            return new QuestionQPR(enonce, categorie, reponse);
        } else if (type == Type.IMAGE) {
            String imagePath = champs[5].trim(); // colonne 6
            String reponse = champs[6].trim();   // colonne 7
            return new QuestionImage(enonce, categorie, imagePath, reponse);
        } else {
            return null;
        }
    }

    public static List<Question> lireQuestions(String nomFichierRessource) throws IOException {
        InputStream in = CsvLecteur.class.getClassLoader().getResourceAsStream(nomFichierRessource);
        if (in == null) {
            throw new FileNotFoundException("Fichier " + nomFichierRessource + " introuvable dans les ressources.");
        }

        List<Question> liste = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String ligne;
            reader.readLine(); // ignorer l'en-tête

            while ((ligne = reader.readLine()) != null) {
                String[] champs = parseCsvLine(ligne);
                if (champs.length >= 7) {
                    Question q = creerQuestion(champs);
                    if (q != null) liste.add(q);
                }
            }
        }

        return liste;
    }

    // Méthode simple pour parser une ligne CSV en gérant les champs entre guillemets
    private static String[] parseCsvLine(String ligne) {
        List<String> resultats = new ArrayList<>();
        StringBuilder champ = new StringBuilder();
        boolean entreGuillemets = false;

        for (int i = 0; i < ligne.length(); i++) {
            char c = ligne.charAt(i);

            if (c == '"') {
                entreGuillemets = !entreGuillemets;
            } else if (c == ',' && !entreGuillemets) {
                resultats.add(champ.toString().trim());
                champ.setLength(0);
            } else {
                champ.append(c);
            }
        }
        resultats.add(champ.toString().trim());
        return resultats.toArray(new String[0]);
    }
}
