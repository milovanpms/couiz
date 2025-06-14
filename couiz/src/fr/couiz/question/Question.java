package fr.couiz.question;

public abstract class Question {
    protected String enonce;
    protected Type type;
    protected CategorieQuestion categorie;

    public Question(String enonce, Type type, CategorieQuestion categorie) {
        this.enonce = enonce;
        this.type = type;
        this.categorie = categorie;
    }

    public String getIntitule() {
        return enonce;
    }

    public Type getType() {
        return type;
    }

    public CategorieQuestion getCategorie() {
        return categorie;
    }

    // Méthode à redéfinir pour retourner les propositions (ou null si QCM non applicable)
    public abstract String[] getPropositions();

    public abstract boolean estBonneReponse(String reponse);

    public abstract int getPoints();

    public abstract void afficher();

    // Ajout méthode abstraite pour récupérer la réponse correcte sous forme texte
    public abstract String getReponseCorrecte();
}
