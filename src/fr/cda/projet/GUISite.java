package fr.cda.projet;

import fr.cda.ihm.*;
import fr.cda.util.Log4j;

/**
 * Classe de definition de l'IHM principal
 */
public class GUISite implements FormulaireInt {
    private final Site site;  // Le site

    /**
     * Constructeur de l'IHM principal
     *
     * @param site site courant envoye en parametre
     */
    public GUISite(Site site) {
        this.site = site;

        // Creation du formulaire
        Formulaire form = new Formulaire("Site de vente", this, 1100, 730);

        //  Creation des elements de l'IHM
        form.addLabel("Afficher tous les produits du stock");
        form.addButton("AFF_STOCK", "Tous le stock");
        form.addLabel("");
        form.addLabel("Afficher tous les bons de commande");
        form.addButton("AFF_COMMANDES", "Toutes les commandes");
        form.addLabel("");
        form.addLabel("Afficher les commandes non livrees");
        form.addButton("NON_LIVREES", "Commandes non livrees");
        form.addLabel("");
        form.addText("NUM_COMMANDE", "Numero de commande", true, "1");
        form.addLabel("");
        form.addLabel("Afficher le detail de la commande");
        form.addButton("AFF_COMMANDE", "Afficher");
        form.addLabel("");
        form.addLabel("Modifier les references de la commande");
        form.addButton("MODIFIER", "Modifier");
        form.addLabel("");
        form.addLabel("Livrer les commandes");
        form.addButton("LIVRER", "Livrer");
        form.addLabel("");
        form.addLabel("Calcul des ventes des commandes livrees");
        form.addButton("CALCUL_VENTES", "Calculer ventes");
        form.addLabel("");
        form.addLabel("Modifier l'etat du stock");
        form.addButton("MODIFIER_STOCK", "Modifier le stock");
        form.addLabel("");
        form.addLabel("Sauvegarde des produits et des commandes");
        form.addButton("SAUVEGARDER", "Sauvegarder");
        form.addLabel("");
        form.addLabel("Fermer l'application");
        form.addButton("FERMER", "Fermer");

        form.setPosition(400, 0);
        form.addZoneText("RESULTATS", "Resultats",
                true,
                "",
                600, 700);

        // Affichage du formulaire
        form.afficher();
    }

    /**
     * Methode appellee quand on clique dans un bouton
     * @param form      Le formulaire dans lequel se trouve le bouton
     * @param nomSubmit Le nom du bouton qui a été utilisé.
     */
    public void submit(Formulaire form, String nomSubmit) {

        // Affichage de tous les produits du stock
        if (nomSubmit.equals("AFF_STOCK")) {
            String res = site.listerTousProduits();
            form.setValeurChamp("RESULTATS", res);
        }

        // Affichage de toutes les commandes
        if (nomSubmit.equals("AFF_COMMANDES")) {
            form.setValeurChamp("RESULTATS", site.listerToutesCommandes());
        }

        // Affichage d'une commande par son numero
        if (nomSubmit.equals("AFF_COMMANDE")) {
            String numStr = form.getValeurChamp("NUM_COMMANDE");
            try {
                int num = Integer.parseInt(numStr);
                String res = site.listerCommande(num);
                form.setValeurChamp("RESULTATS", res);
            } catch (NumberFormatException e) {
                String res = "Veuillez entrer un numéro de commande";
                form.setValeurChamp("RESULTATS", res);
                Log4j.logger.info(res + ", " + e);
            }
        }

        // appel de l'IHM de modification de commande
        if (nomSubmit.equals("MODIFIER")) {
            String numStr = form.getValeurChamp("NUM_COMMANDE");
            String erreur;
            try {
                int num = Integer.parseInt(numStr);
                // on recupere la commande par son numero
                Commande commande = site.trouverCommande(num);
                // on verifie que le commande n'est pas livree
                if (!commande.isEtatLivraison()) {
                    // instanciation de l'ihm de modification de commande
                    new GUIModifierCommande(site, commande);
                } else {
                    form.setValeurChamp("RESULTATS", "Cette commande a deja ete livree, impossible de l'a modifier.");
                }
            } catch (NumberFormatException e) {
                erreur = "Veuillez entrer un numero de commande valide";
                form.setValeurChamp("RESULTATS", erreur);
                Log4j.logger.info(erreur + ", " + e);
            } catch (NullPointerException e) {
                erreur = "Aucune commande n'a ete trouvee avec ce numero";
                form.setValeurChamp("RESULTATS", erreur);
                Log4j.logger.info(erreur + ", " + e);
            }
        }

        // Affichage de toutes les commandes non livrees
        if (nomSubmit.equals("NON_LIVREES")) {
            form.setValeurChamp("RESULTATS", site.commandesNonLivrees());
        }

        // Livraison de toutes les commandes non livrees
        if (nomSubmit.equals("LIVRER")) {
            String erreur;
            try {
                String res = site.livrerToutesCommandes();
                form.setValeurChamp("RESULTATS", res);
            } catch (NullPointerException e) {
                erreur = "Aucune commande n'a ete trouvee avec ce numero";
                form.setValeurChamp("RESULTATS", erreur);
                Log4j.logger.info(erreur + ", " + e);
            } catch (NumberFormatException e) {
                erreur = "Veuillez entrer un numero de commande valide";
                form.setValeurChamp("RESULTATS", erreur);
                Log4j.logger.info(erreur + ", " + e);
            } catch (ArrayIndexOutOfBoundsException e) {
                erreur = "La commande ne possede pas de references";
                form.setValeurChamp("RESULTATS", erreur);
                Log4j.logger.info(erreur + ", " + e);
            }
        }

        // appel de l'IHM de modification de stock
        if (nomSubmit.equals("MODIFIER_STOCK")) {
            new GUIModifierStock(site);
        }

        // Calcul des ventes des commandes livrees
        if (nomSubmit.equals("CALCUL_VENTES")) {
            form.setValeurChamp("RESULTATS", site.calculerVentes());
        }

        // Sauvegarde des produits du stock et des bons de commandes
        if (nomSubmit.equals("SAUVEGARDER")) {
            site.sauvegarder();
            form.setValeurChamp("RESULTATS", "Les produits du stock et les bons de commandes ont bien ete sauvegardes.");
        }

        // Fermer l'application
        if (nomSubmit.equals("FERMER")) {
            site.sauvegarder();
            form.fermer();
        }
    }
}