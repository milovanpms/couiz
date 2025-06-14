package fr.couiz.utilitaire;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Classement {

    private ArrayList<Enregistrement> liste = new ArrayList<>();

    public ArrayList<Enregistrement> getListe() {
        return liste;
    }

    public void setListe(ArrayList<Enregistrement> liste) {
        this.liste = liste;
    }

    public void reset() {
        this.liste.clear();
    }

    public void ajouter(String pseudo, int score) {
        this.liste.add(new Enregistrement(pseudo, score));
    }

    public void ajouter(Enregistrement nouveau) {
        this.liste.add(nouveau);
    }

    public void afficher() {
        if (liste.isEmpty()) {
            System.out.println("Aucun score enregistré.");
            return;
        }

        System.out.println("\n--- Classement Général ---");
        liste.stream()
                .sorted(Comparator.comparingInt(Enregistrement::getScore).reversed())
                .forEach(e -> System.out.println(e.getPseudo() + " : " + e.getScore() + " pts"));
    }

    /** Charge un fichier CSV situé en dehors des ressources (ex : à côté du package fr.couiz). */
    public void chargerDepuisFichier(String cheminFichier) {
        liste.clear();
        File fichier = new File(cheminFichier);

        if (!fichier.exists()) {
            System.err.println("Fichier introuvable : " + cheminFichier);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier, StandardCharsets.UTF_8))) {
            String ligne;
            reader.readLine(); // ignore l'en-tête

            while ((ligne = reader.readLine()) != null) {
                String[] parties = ligne.trim().split(",");
                if (parties.length != 2) continue;

                String pseudo = parties[0].trim();
                if (pseudo.equalsIgnoreCase("pseudo")) continue;

                try {
                    int score = Integer.parseInt(parties[1].trim());
                    liste.add(new Enregistrement(pseudo, score));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier : " + e.getMessage());
        }
    }

    /** Sauvegarde dans un fichier local situé à côté du package, pas dans ressources. */
    public void sauvegarder(String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier, StandardCharsets.UTF_8))) {
            writer.write("pseudo,score");
            writer.newLine();
            for (Enregistrement e : liste) {
                writer.write(e.getPseudo() + "," + e.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du fichier : " + e.getMessage());
        }
    }

    public void trierParScoreDecroissant() {
        liste.sort(Comparator.comparingInt(Enregistrement::getScore).reversed());
    }

    /**
     * Initialise le classement : charge depuis un fichier local ou depuis une ressource si le fichier local n'existe pas.
     */
    public void initialiser(String cheminFichierLocal, String nomFichierRessource) {
        File fichierLocal = new File(cheminFichierLocal);

        if (fichierLocal.exists()) {
            System.out.println("Chargement depuis le fichier local : " + cheminFichierLocal);
            chargerDepuisFichier(cheminFichierLocal);
        } else {
            System.out.println("Fichier local introuvable. Chargement depuis les ressources : " + nomFichierRessource);
            charger(nomFichierRessource);
            sauvegarder(cheminFichierLocal);
            System.out.println("Fichier local créé à partir des ressources : " + cheminFichierLocal);
        }
    }
    /** Charge un fichier CSV à partir des ressources intégrées au projet (lecture seule) */
    public void charger(String nomFichier) {
        liste.clear();
        InputStream in = getClass().getClassLoader().getResourceAsStream(nomFichier);

        if (in == null) {
            System.err.println("Fichier " + nomFichier + " introuvable dans les ressources.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String ligne;
            reader.readLine(); // ignore l'en-tête

            while ((ligne = reader.readLine()) != null) {
                String[] parties = ligne.trim().split(",");
                if (parties.length != 2) continue;

                String pseudo = parties[0].trim();
                if (pseudo.equalsIgnoreCase("pseudo")) continue;

                try {
                    int score = Integer.parseInt(parties[1].trim());
                    liste.add(new Enregistrement(pseudo, score));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement depuis les ressources : " + e.getMessage());
        }
    }
}
