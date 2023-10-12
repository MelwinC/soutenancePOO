package fr.cda.projet;
import java.util.*;

/**
 * Classe de definition d'un bon de commande
 */
public class Commande {
    /**
     * Les caracteristiques d'une commande (attributs)
     */
    private int numero;
    private String date;
    private String client;
    private ArrayList<String> references;
    private boolean etatLivraison = false;  // etat de livraison de la commande (false par defaut)
    private String raison;                  // raison de pourquoi la commande n'a pas ete livree

    /**
     * @param numero - numero de la commande
     * @param date - date de la commande. Au format JJ/MM/AAAA
     * @param client - nom du client
     * @param references - les references des produits de la commande
     * Constructeur de commande
     */
    public Commande(int numero, String date, String client, ArrayList<String> references) {
        this.numero = numero;
        this.date = date;
        this.client = client;
        this.references = references;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public ArrayList<String> getReferences() {
        return references;
    }

    public void setReferences(ArrayList<String> references) {
        this.references = references;
    }

    public boolean isEtatLivraison() {
        return etatLivraison;
    }

    public void setEtatLivraison(boolean etatLivraison) {
        this.etatLivraison = etatLivraison;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    /**
     * Surcharge de la methode toString, affichage conditionnel en fonction de l'etat de livraison et de la raison
     */
    @Override
    public String toString() {
        return "Commande n°" + numero + '\n' + "Date: " + date + '\n' + "Client: " + client + '\n' + "References: " + references + '\n' + (etatLivraison ? "Livree" : (raison == null ? "Livrable" : "Non Livree" + '\n' +  "Raison : " + raison));
    }
}