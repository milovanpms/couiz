package fr.couiz.utilitaire;

public class GestionAleatoire {
    private int PetiteBorne = 0;
    private int GrandeBorne = 49;

    public int getGrandeBorne() {
        return GrandeBorne;
    }

    public void setGrandeBorne(int grandeBorne) {
        GrandeBorne = grandeBorne;
    }

    public int getPetiteBorne() {
        return PetiteBorne;
    }

    public void setPetiteBorne(int petiteBorne) {
        PetiteBorne = petiteBorne;
    }

    public GestionAleatoire() {
    }

    // Méthode qui génère un nombre entre PetiteBorne et GrandeBorne inclus
    public int nombreAleatoire() {
        int min = getPetiteBorne();
        int max = getGrandeBorne();
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    // Nouvelle méthode : nombre aléatoire entre min et max inclus (plus pratique)
    public int nombreAleatoire(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min doit être inférieur ou égal à max");
        }
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}