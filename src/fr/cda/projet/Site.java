package fr.cda.projet;

import java.util.*;
import fr.cda.util.*;

// Classe de definition du site de vente
public class Site {
    private ArrayList<Produit> stock;       // Les produits du stock
    private ArrayList<Commande> commandes;  // Les bons de commande

    // Constructeur
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

    // retourne les produits du stock en String
    public String listerTousProduits() {
        StringBuilder res = new StringBuilder();
        for (Produit prod : stock) {
            res.append(prod.toString()).append("\n");
        }
        return res.toString();
    }

    // retourne les commandes en String
    public String listerToutesCommandes() {
        StringBuilder res = new StringBuilder();
        for (Commande com : commandes) {
            res.append(com.toString()).append("\n");
            res.append("----------------------" + '\n');
        }
        return res.toString();
    }

    // recherche une commande par son numero et retourne celle-ci en String
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

    // recherche une commande par son numero et retourne celle-ci en String
    public Commande trouverCommande(int numero) {
        Commande commande = null;
        for (Commande c : commandes) {
            if (c.getNumero() == numero) {
                commande = c;
            }
        }
        return commande;
    }

    // retourne les commandes non livrees en String
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

    // lis le fichier envoye en argument et ajoute au stock tous les produits
    private void initialiserStock(String nomFichier) {
        String[] lignes = Terminal.lireFichierTexte(nomFichier);
        assert lignes != null;
        for (String ligne : lignes) {
            String[] champs = ligne.split(";", 4);
            String reference = champs[0];
            String nom = champs[1];
            double prix = Double.parseDouble(champs[2]);
            int quantite = Integer.parseInt(champs[3]);
            Produit p = new Produit(reference, nom, prix, quantite);
            stock.add(p);
        }
    }

    // lis le fichier envoye en argument et ajoute les commandes du fichier
    private void initialiserCommandes(String nomFichier) {
        String[] lignes = Terminal.lireFichierTexte(nomFichier);
        int numeroCourant;
        int numeroPrecedent = -1;
        ArrayList<String> references = new ArrayList<>();
        Commande c;
        assert lignes != null;
        for (String ligne : lignes) {
            String[] champs = ligne.split(";", 4);
            numeroCourant = Integer.parseInt(champs[0]);
            String date = champs[1];
            String nom = champs[2];
            if (numeroCourant != numeroPrecedent) {
                references = new ArrayList<>();
                c = new Commande(numeroCourant, date, nom, references);
                commandes.add(c);
            }
            references.add(champs[3]);
            numeroPrecedent = numeroCourant;
        }
    }

    public void initialiserRaisonsCommandes() {
        for (Commande c : commandes) {
            if (!c.isEtatLivraison()) {
                StringBuilder res = new StringBuilder();
                for (String ref : c.getReferences()) {
                    String[] refSplit = ref.split("=");
                    String nomRef = refSplit[0];
                    int quantiteC = Integer.parseInt(refSplit[1]);
                    Produit p = trouverProduit(nomRef);
                    if (quantiteC - p.getQuantite() > 0) {
                        res.append('\n').append(" - il manque ").append(quantiteC - p.getQuantite()).append(" produits de type '").append(nomRef).append("'");
                    }
                }
                if (!res.isEmpty()) {
                    c.setRaison(String.valueOf(res));
                }
            }
        }
    }

    public Produit trouverProduit(String nomReference) {
        Produit res = null;
        for (Produit p : stock) {
            if (nomReference.equals(p.getReference())) {
                res = p;
            }
        }
        return res;
    }

    public String calculerVentes() {
        double total = 0;
        StringBuilder res = new StringBuilder();
        for (Commande commande : commandes) {
            if (commande.isEtatLivraison()) {
                ArrayList<String> references = commande.getReferences();
                res.append("Commande : ").append(commande.getNumero()).append('\n').append('\n');
                for (String reference : references) {
                    String[] tabReference = reference.split("=");
                    String nomReference = tabReference[0];
                    int quantite = Integer.parseInt(tabReference[1]);
                    Produit p = trouverProduit(nomReference);
                    double prix = p.getPrix();
                    res.append(p.getNom()).append("  ").append(quantite).append(" x ").append(p.getPrix()).append(" €");
                    res.append('\n');
                    total += prix * quantite;
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

    public String livrer(Commande commande) {
        String res = "";
        StringBuilder raisonRefus = new StringBuilder();
        if (!commande.isEtatLivraison()) {
            for (String ref : commande.getReferences()) {
                String[] refSplit = ref.split("=");
                String nomRef = refSplit[0];
                int quantite = Integer.parseInt(refSplit[1]);
                Produit p = trouverProduit(nomRef);
                if (quantite - p.getQuantite() > 0) {
                    res = "La commande " + commande.getNumero() + " ne peut pas etre livree car il manque : " + '\n';
                    raisonRefus.append("    - ").append(quantite).append(" produits de type : ").append(nomRef).append('\n');
                } else {
                    p.setQuantite(p.getQuantite() - quantite);
                }
            }
            if (res.isEmpty()) {
                res = "La commande " + commande.getNumero() + " a bien ete livree." + '\n';
                res += ("------------------------------------------------------------" + '\n');
                commande.setEtatLivraison(!commande.isEtatLivraison());
            } else {
                res = res + raisonRefus;
                res += ("------------------------------------------------------------" + '\n');
            }
        }
        initialiserRaisonsCommandes();
        return res;
    }

    public String livrerToutesCommandes() {
        StringBuilder res = new StringBuilder();
        for (Commande c : commandes) {
            res.append(livrer(c));

        }
        return res.toString();
    }

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