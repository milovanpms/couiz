package fr.couiz.utilitaire;

import java.time.LocalTime;

public class Minuterie extends Thread {
    private int tempsDelais;     // durée totale en secondes
    private int tempsActuel = 0; // temps restant
    private int tempsFin;        // heure en secondes depuis minuit
    private volatile boolean termine = false;
    private volatile boolean enCours = false;

    public Minuterie() {
        this.tempsDelais = 90;
    }

    public void debut(int dureeSecondes) {
        this.tempsDelais = dureeSecondes;
        int maintenant = LocalTime.now().toSecondOfDay();
        this.tempsFin = maintenant + dureeSecondes;
        this.enCours = true;
        this.termine = false;
        this.start();  // Démarre le thread automatiquement
    }

    @Override
    public void run() {
        while (!termine && !estTerminee()) {
            mettreAJour();
            try {
                Thread.sleep(1000); // mise à jour chaque seconde
            } catch (InterruptedException e) {
                System.out.println(" Minuterie interrompue.");
                return;
            }
        }
        this.termine = true;
        System.out.println("\nTemps écoulé ! (appuyer sur entrée)");
    }

    public void mettreAJour() {
        int maintenant = LocalTime.now().toSecondOfDay();
        this.tempsActuel = tempsFin - maintenant;
        if (tempsActuel <= 0) {
            tempsActuel = 0;
            termine = true;
        }
    }

    public boolean estTerminee() {
        return termine;
    }

    public int getTempsActuel() {
        mettreAJour();
        return tempsActuel;
    }

    public int getTempsDelais() {
        return tempsDelais;
    }

    public int getTempsFin() {
        return tempsFin;
    }

    public void arreter() {
        this.termine = true;
        this.interrupt();  // si elle dort, on l’interrompt
    }

    public boolean isEnCours() {
        return enCours && !termine;
    }
}
