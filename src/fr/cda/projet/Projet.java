package fr.cda.projet;

/**
 * Classe principale d'execution du projet
 */
public class Projet {

    /**
     * Methode main, lancement du programme
     */
    public static void main(String[] args) {
        Site site = new Site();
        // instanciation de l'IHM principal
        new GUISite(site);
    }
}
