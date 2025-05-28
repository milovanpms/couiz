package fr.couiz.question;

public abstract class Question {
    String question;
    String reponseCorrecte;
    int points;
    String categorie;
    String[] reponsesAlternatives; // Pour les synonymes et variantes genre USA = US = Amérique

    // Constructeur sans réponses alternatives
    public Question(String question, String reponseCorrecte, int points, String categorie) {
        this.question = question;
        this.reponseCorrecte = reponseCorrecte;
        this.points = points;
        this.categorie = categorie;
        this.reponsesAlternatives = new String[0];
    }

    // Constructeur avec réponses alternatives
    public Question(String question, String reponseCorrecte, int points, String categorie, String[] reponsesAlternatives) {
        this.question = question;
        this.reponseCorrecte = reponseCorrecte;
        this.points = points;
        this.categorie = categorie;
        this.reponsesAlternatives = reponsesAlternatives;
    }

    public int getPoints() {
        return points;
    }

    public String getQuestion() {
        return question;
    }

    public String getCategorie() {
        return categorie;
    }
    // TODO: boolean getCorrectAnswer() pour vérifier si la réponse de l'utilisateur est correcte (par défaut false)
}