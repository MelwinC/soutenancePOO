package fr.cda.projet;

import java.util.*;
import fr.cda.util.*;

/**
 * Classe de definition du site de vente
 */
public class Site {
    private ArrayList<Produit> stock;
    private final ArrayList<Commande> commandes;

    /**
     * Constructeur du site qui contient toutes les methodes de gestion du stock et des bons de commandes
     */
    public Site() {
        stock = new ArrayList<>();
        commandes = new ArrayList<>();

        // initialisation du stock avec de la data
        initialiserStock("data/Produits.txt");

        // initialisation des commandes avec de la data
        initialiserCommandes("data/Commandes.txt");

        // initialisation des raisons des commandes non livrees
        initialiserRaisonsCommandes();
    }

    public ArrayList<Produit> getStock() {
        return stock;
    }

    public void setStock(ArrayList<Produit> stock) {
        this.stock = stock;
    }

    /**
     * @return une String de tous les produits de l'arraylist, fait appel a la methode toString de Produit
     */
    public String listerTousProduits() {
        StringBuilder res = new StringBuilder();
        for (Produit prod : stock) {
            res.append(prod.toString()).append("\n");
        }
        return res.toString();
    }

    /**
     * @return une String de toutes les commandes de l'arraylist, fait appel a la methode toString de Commande
     */
    public String listerToutesCommandes() {
        StringBuilder res = new StringBuilder();
        for (Commande com : commandes) {
            res.append(com.toString()).append("\n");
            res.append("----------------------" + '\n');
        }
        return res.toString();
    }

    /**
     * @param numero numero de commande est unique
     * @return une String de la Commande qui possede le numero envoye en argument, fait appel a la methode toString de Commande
     */
    public String listerCommande(int numero) {
        StringBuilder res = new StringBuilder();
        boolean trouve = false;
        for (Commande c : commandes) {
            if (c.getNumero() == numero) {
                res.append(c);
                trouve = true;
            }
        }
        if (!trouve) {
            res.append("Aucune commande n'a ete trouvee avec ce numero.");
        }
        return res.toString();
    }

    /**
     * @param numero numero de commande est unique
     * @return un objet de type Commande qui possede le numero envoye en argument
     */
    public Commande trouverCommande(int numero) {
        Commande commande = null;
        for (Commande c : commandes) {
            if (c.getNumero() == numero) {
                commande = c;
            }
        }
        return commande;
    }

    /**
     * filtre les commandes qui ont comme attribut etatLivraison == false de l'arraylist commandes
     * @return une String de toutes ces commandes, fait appel a la methode toString de Commande
     */
    public String commandesNonLivrees() {
        StringBuilder res = new StringBuilder();
        boolean trouve = false;
        for (Commande c : commandes) {
            if (!c.isEtatLivraison()) {
                res.append(c).append("\n");
                res.append("----------------------" + '\n');
                trouve = true;
            }
        }
        if (!trouve) {
            res.append("Toutes les commandes ont ete livrees.");
        }
        return res.toString();
    }

    /**
     * Lecture du fichier des produits et ajoute chaque produit a l'arraylist stock
     * @param nomFichier nom du fichier produits.txt envoye en argument
     */
    private void initialiserStock(String nomFichier) {
        try {
            String[] lignes = Terminal.lireFichierTexte(nomFichier);
            assert lignes != null;
            for (String ligne : lignes) {
                // separe tous les champs du fichier texte delimites par les ;
                String[] champs = ligne.split(";", 4);
                String reference = champs[0];
                String nom = champs[1];
                double prix = Double.parseDouble(champs[2]);
                int quantite = Integer.parseInt(champs[3]);
                // creation d'un objet Produit avec les informations du fichier texte
                Produit p = new Produit(reference, nom, prix, quantite);
                // ajout de l'objet Produit dans le stock
                stock.add(p);
            }
        } catch (NumberFormatException e) {
            Log4j.logger.info(e);
        }
    }

    /**
     * Lecture du fichier des bons de commandes et ajoute chaque produit a l'arraylist commandes
     * @param nomFichier nom du fichier commandes.txt envoye en argument
     */
    private void initialiserCommandes(String nomFichier) {
        try {
            String[] lignes = Terminal.lireFichierTexte(nomFichier);
            int numeroCourant;
            int numeroPrecedent = -1;
            // contient toutes les references d'un bon de commande
            ArrayList<String> references = new ArrayList<>();
            Commande c;
            assert lignes != null;
            for (String ligne : lignes) {
                // separe tous les champs du fichier texte delimites par les ;
                String[] champs = ligne.split(";", 4);
                numeroCourant = Integer.parseInt(champs[0]);
                String date = champs[1];
                String nom = champs[2];
                // on utilise un numero courant / precedent pour verifier si la prochaine ligne a toujours le meme numero de commande
                // si ce n'est pas le cas
                if (numeroCourant != numeroPrecedent) {
                    // on cree un nouvel arraylist de references (pour vider le precedent)
                    references = new ArrayList<>();
                    // on instancie l'objet Commande avec tous les parametres assignes precedemment
                    c = new Commande(numeroCourant, date, nom, references);
                    // et on ajoute l'objet Commande a l'arraylist commandes
                    commandes.add(c);
                }
                references.add(champs[3]);
                // on assigne le numeroCourant au numeroPrecedent pour le comparer a la prochaine boucle
                numeroPrecedent = numeroCourant;
            }
        } catch (NumberFormatException e) {
            Log4j.logger.info(e);
        }
    }

    /**
     * definit les raisons pour lesquelle les commandes ne peuvent pas etre livrees (quantite de produits manquante)
     */
    public void initialiserRaisonsCommandes() {
        try {
            for (Commande c : commandes) {
                // on ne regarde que les commandes qui ne sont pas livrees
                if (!c.isEtatLivraison()) {
                    StringBuilder res = new StringBuilder();
                    for (String ref : c.getReferences()) {
                        // on separe le nom de la reference et sa quantite afin de pouvoir l'exploiter par la suite
                        String[] refSplit = ref.split("=");
                        String nomRef = refSplit[0];
                        int quantiteC = Integer.parseInt(refSplit[1]);
                        // on recupere le Produit qui a le meme nom de reference
                        Produit p = trouverProduit(nomRef);
                        // si il n'y a pas la quantite suffisante de ce Produit en stock, alors on recupere l'information de ce qu'il manque
                        if (quantiteC - p.getQuantite() > 0) {
                            res.append('\n').append(" - il manque ").append(quantiteC - p.getQuantite()).append(" produits de type '").append(nomRef).append("'");
                        }
                    }
                    // si la string n'est pas vide, cela signifie qu'il manque au moins un produit
                    if (!res.isEmpty()) {
                        // on ajoute cette raison a la commande
                        c.setRaison(String.valueOf(res));
                    } else {
                        c.setRaison(null);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log4j.logger.info("Une des commandes ne possede pas de references, " + e);
        }
    }

    /**
     * @param nomReference nom de la reference d'un produit, se trouvant dans une commande
     * @return le Produit trouve qui possede ce nom de reference en parcourant tout le stock
     */
    public Produit trouverProduit(String nomReference) {
        Produit res = null;
        for (Produit p : stock) {
            if (nomReference.equals(p.getReference())) {
                res = p;
            }
        }
        return res;
    }

    /**
     * parcours toutes les commandes livrees et calcule le total de ces ventes
     * @return le calcul + la valeur en String
     */
    public String calculerVentes() {
        double total = 0;
        StringBuilder res = new StringBuilder();
        for (Commande commande : commandes) {
            // parcours que les commandes livrees
            if (commande.isEtatLivraison()) {
                ArrayList<String> references = commande.getReferences();
                res.append("Commande : ").append(commande.getNumero()).append('\n').append('\n');
                for (String reference : references) {
                    // on separe le nom de la reference et la quantite
                    String[] tabReference = reference.split("=");
                    String nomReference = tabReference[0];
                    int quantiteC = Integer.parseInt(tabReference[1]);
                    // pour chaque reference de la commande on cherche le Produit correspondant
                    Produit p = trouverProduit(nomReference);
                    // on recupere son prix unitaire
                    double prix = p.getPrix();
                    res.append(p.getNom()).append("  ").append(quantiteC).append(" x ").append(p.getPrix()).append(" €");
                    res.append('\n');
                    // on calcule le prix unitaire * la quantite presente dans la commande
                    total += prix * quantiteC;
                }
                res.append("--------------------").append('\n');
            }
        }
        if (total == 0) {
            res.append("Aucune commande n'a ete effectuee, le total des ventes vaut donc 0.0 €");
        } else {
            res.append('\n').append("Somme : ").append(total).append(" €");
        }
        return res.toString();
    }

    /**
     * Livre la commande si elle est livrable
     * @param commande que l'on souhaite livrer envoyee en parametre
     * @return en String si la commande a ete livree ou sinon la raison pour laquelle ce n'est pas possible
     */
    public String livrer(Commande commande) {
        String res = "";
        StringBuilder raisonRefus = new StringBuilder();
        // on verifie que la commande n'est pas deja livree
        if (!commande.isEtatLivraison()) {
            for (String ref : commande.getReferences()) {
                // on separe le nom de la reference et la quantite
                String[] refSplit = ref.split("=");
                String nomRef = refSplit[0];
                int quantite = Integer.parseInt(refSplit[1]);
                Produit p = trouverProduit(nomRef);
                // la commande ne peut pas etre livree car il manque des elements en stock
                if (quantite - p.getQuantite() > 0) {
                    res = "La commande " + commande.getNumero() + " ne peut pas etre livree car il manque : " + '\n';
                    raisonRefus.append("    - ").append(quantite).append(" produits de type : ").append(nomRef).append('\n');
                }
            }
            // la commande peut etre livree
            if (res.isEmpty()) {
                for (String ref : commande.getReferences()) {
                    String[] refSplit = ref.split("=");
                    String nomRef = refSplit[0];
                    int quantite = Integer.parseInt(refSplit[1]);
                    Produit p = trouverProduit(nomRef);
                    // on ajuste le stock
                    p.setQuantite(p.getQuantite() - quantite);
                }
                res = "La commande " + commande.getNumero() + " a bien ete livree." + '\n';
                res += ("------------------------------------------------------------" + '\n');
                // on livre la commande
                commande.setEtatLivraison(!commande.isEtatLivraison());
            } else { // la commande ne peut pas etre livree
                // on retourne la ou les raison(s) de refus
                res = res + raisonRefus;
                res += ("------------------------------------------------------------" + '\n');
            }
        }
        // on refresh les raisons des commandes
        initialiserRaisonsCommandes();
        return res;
    }

    /**
     * @return on applique la methode Livrer(commande) a tout l'arraylist commandes
     */
    public String livrerToutesCommandes() {
        StringBuilder res = new StringBuilder();
        for (Commande c : commandes) {
            res.append(livrer(c));
        }
        return res.toString();
    }

    /**
     * effectue la sauvegarde de tous les produits et les commandes dans les fichiers txt correspondants
     */
    public void sauvegarder() {
        StringBuffer sbProduits = new StringBuffer();
        StringBuffer sbCommandes = new StringBuffer();
        for (Produit p : stock) {
            sbProduits.append(p.getReference()).append(";");
            sbProduits.append(p.getNom()).append(";");
            sbProduits.append(p.getPrix()).append(";");
            sbProduits.append(p.getQuantite()).append('\n');
        }
        for (Commande c : commandes) {
            for (String ref : c.getReferences()) {
                sbCommandes.append(c.getNumero()).append(";");
                sbCommandes.append(c.getDate()).append(";");
                sbCommandes.append(c.getClient()).append(";");
                sbCommandes.append(ref).append('\n');
            }
        }
        Terminal.ecrireFichier("data/Produits.txt", sbProduits);
        Terminal.ecrireFichier("data/Commandes.txt", sbCommandes);
    }
}