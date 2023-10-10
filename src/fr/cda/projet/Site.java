package fr.cda.projet;

import java.lang.reflect.Array;
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
        System.out.println(commandes);

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
                ;
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
        System.out.println(Arrays.toString(lignes));
        int numeroCourant;
        int numeroPrecedent = -1;
        ArrayList<String> references = new ArrayList<>();
        Commande c;
        for (int i = 0; i < lignes.length; i++) {
            String[] champs = lignes[i].split(";", 4);
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

    public String calculVente(Commande commande) {
        ArrayList<String> references = commande.getReferences();
        double total = 0;
        StringBuffer res = new StringBuffer();
        res.append("Commande : ").append(commande.getNumero()).append('\n');
        for (int i = 0; i < references.size(); i++) {
            String[] reference = references.get(i).split("=");
            String nomReference = reference[0];
            int quantite = Integer.parseInt(reference[1]);
            Produit p = trouverProduit(nomReference);
            double prix = p.getPrix();
            res.append(p.getNom()).append("  ").append(quantite).append("  ").append(p.getPrix()).append(" €");
            res.append('\n');
            total += prix * quantite;
        }
        res.append("==================").append('\n').append("Somme : ").append(total).append(" €");
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
                    raisonRefus.append(" - ").append(quantite).append(" produits de type : ").append(nomRef).append('\n');
                } else {
                    p.setQuantite(p.getQuantite() - quantite);
                }
            }
            if (res.isEmpty()) {
                res = "La commande " + commande.getNumero() + " a bien ete livree." + '\n';
                commande.setEtatLivraison(!commande.isEtatLivraison());
            } else {
                res = res + raisonRefus;
            }
        } else {
            res = "La commande a deja ete livree." +  '\n';
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
}