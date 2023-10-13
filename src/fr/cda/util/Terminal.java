package fr.cda.util;

import java.io.*;
import java.util.*;

/** La classe Terminal permet de realiser ses premiers programmes Java en permettant d'afficher dans la console d'execution des donnees de type differents, et en permettant de saisir au clavier des donnees de type differents.<BR>
    Elle permet aussi de lire et ecrire un fichier texte
    Cette classe contient que des methodes statiques. */
public class Terminal{    

    // Le buffer standard  de lecture = le clavier
    private static BufferedReader in =
        new BufferedReader(new InputStreamReader(System.in));

    /** Cette methode lit un fichier texte et retourne le contenu du fichier sous la forme d'un  tableau de String dont chaque element est une ligne du fichier.
        @param nomFichier le nom du fichier qui doit etre dans le repertoire courant.
        @return String[] le contenu du fichier. 
        @exception TerminalException (de type RuntimeException) si erreur d'ecriture<BR>
        Rappel : Une exception de type RuntimeException n'a pas l'obligation d'etre capturee.
*/
    public static String[] lireFichierTexte(String nomFichier)
    {
        try{
            File fichier = new File(nomFichier);
            FileInputStream fis = new FileInputStream(new File(nomFichier));
            
            byte[] buffer = new byte[(int)fichier.length()];
            fis.read(buffer);
            fis.close();
            String str = new String(buffer);

            // On enleve le caractere '\r' code 13 qui est ajoute en Windows
            // Les fins de ligne dans un fichier texte cree sous Windows
            //  se termine par \r\n.
            // Il faut enlever le \r car il a des effets perturbant sur
            //  la methode System.out.print et est pris comme un caractere de plus
            //  qu'il faut eliminer
            //  
            String texte = str.replaceAll(""+(char)(13),"");
            
            // Les lignes du fichier sont mises dans un tableau
            //
            String[] mots = texte.split("\n");

            return(mots);
        }
        catch(Exception ex) {return null;}
    }

    /** Cette methode permet de creer un fichier texte a partir du contenu d'un StringBuffer.
        @param nomFichier Le nom du fichier qui est cree dans le repertoire courant
        @param strbuf Le StringBuffer contenant le texte a ecrire. 
        @exception TerminalException (de type RuntimeException) si erreur d'ecriture
    */
    public static void ecrireFichier(String nomFichier,
                                     StringBuffer strbuf)
    {
        try{
            File fichier = new File(nomFichier);
            FileOutputStream fos = new FileOutputStream(new File(nomFichier));
            
            byte[] buffer = strbuf.toString().getBytes();
            fos.write(buffer);
            fos.close();
        }
        catch(Exception ex)
            {
                exceptionHandler(ex);
            }
    }

    /** Cette methode lit une chaine de caractere
        @return String la chaine saisie dans la console d'execution
       @exception TerminalException (de type RuntimeException) si erreur de lecture
    */
    public static String lireString() // Lire un String
    {
        String tmp="";
        char C='\0';
        try {
            tmp = in.readLine();
        }
        catch (IOException e)
            {
                exceptionHandler(e);
            }
        return tmp;
    }

    /** Cette methode lit un entier
        @return int L'entier saisi dans la console d'execution
       @exception TerminalException (de type RuntimeException) si la saisie n'est pas un entier ou erreur de lecture
    */
    public static int lireInt()  // Lire un entier
    {
        int x=0;
        try {
            x=Integer.parseInt(lireString());
        }
        catch (NumberFormatException e) {
            exceptionHandler(e);
        }	
        return x ;
    }

    /** Cette methode lit un boolean (false ou true)
        @return boolean Le boolean saisi dans la console d'execution
       @exception TerminalException (de type RuntimeException) si erreur de lecture. <BR>
       Tout autre valeur que TRUE, FALSE, true ou false, retourne la valeur false
    */
    public static boolean lireBoolean()  // Lire un entier
    {
        boolean b = true;
        try {
            b = Boolean.valueOf(lireString()).booleanValue();
        }
        catch (NumberFormatException e) {
            exceptionHandler(e);
        }	
        return b;
    }

    /** Cette methode lit un double
        @return double Le double saisi dans la console d'execution
       @exception TerminalException (de type RuntimeException) si la valeur saisie n'est pas un double ou ereur de lecture.
    */
    public  static double lireDouble()  // Lire un double
    {
        double x=0.0;
        try {
            x=Double.valueOf(lireString()).doubleValue();
        }
        catch (NumberFormatException e) {
            exceptionHandler(e);
        }	
        return x ;
    }

    /** Cette methode lit un caractere.
       @exception TerminalException (de type RuntimeException) si erreur de lecture.<BR>
       Si on saisit plus d'1 caractere alors le caractere retourne est le premier.
    */
    public  static char lireChar()  // Lire un caractere
    {
        String tmp=lireString();
        if (tmp.length()==0)
            return '\n';
        else 
            {
                return tmp.charAt(0);
            }
    }

    /** Cette methode ecrit une chaine et ne revient pas a la ligne.
        @param s la chaine &agrave; &eacute;crire
    */
    public static void ecrireString(String s){ // Afficher un String
        System.out.print(s);
    }

    /** Cette methode ecrit une chaine et revient a la ligne.
        @param s la chaine &agrave; &eacute;crire
    */
    public static void ecrireStringln(String s) // Afficher un String
    {
        ecrireString(s);
        sautDeLigne();
    }

    /** Cette methode ecrit un entier et ne revient pas a la ligne.
        @param i l'entier a ecrire
    */
    public static void ecrireInt(int i)  // Afficher un entier
    {
        ecrireString(""+i);
    }

    /** Cette methode ecrit un entier et revient a la ligne.
        @param i l'entier a ecrire
    */
    public static void ecrireIntln(int i)  // Afficher un entier
    {
        ecrireString(""+i);
        sautDeLigne();
    }

    /** Cette methode ecrit un boolean et ne revient pas a la ligne.
        @param b le booleen a ecrire
    */
    public static void ecrireBoolean(boolean b){
        ecrireString(""+b);
    }

    /** Cette methode ecrit un boolean et revient a la line.
        @param b le booleen a ecrire
    */
    public static void ecrireBooleanln(boolean b){
        ecrireString(""+b);
        sautDeLigne();
    }

    /** Cette methode ecrit un double et ne revient pas a la ligne.
        @param d le double a ecrire
    */
    public  static void ecrireDouble(double d)  // Afficher un double
    {
        ecrireString(""+d);
    }

    /** Cette methode ecrit un double et revient a la ligne.
        @param d le double a ecrire
    */
    public  static void ecrireDoubleln(double d)  // Afficher un double
    {
        ecrireDouble(d);
        sautDeLigne();
    }

    /** Cette methode ecrit un caractere et ne revient pas a la ligne.
        @param c le caractere a ecrire
    */
    public  static void ecrireChar(char c)  // Afficher un caractere
    {
        ecrireString(""+c);
    }  

    /** Cette methode ecrit un caractere et revient a la ligne.
        @param c le caractere a ecrire
    */
    public  static void ecrireCharln(char c)  // Afficher un caractere
    {
        ecrireChar(c);
        sautDeLigne();
    }

    /** Cette methode revient a la ligne.
    */
    public static void sautDeLigne(){
        try{
            System.out.println();
        }catch(Exception ex){
            exceptionHandler(ex);
        }
    }

    /** Cette methode retourne l'exception TerminalException
    */
    protected static void exceptionHandler(Exception ex){
        TerminalException err = new TerminalException(ex);
        throw err;
    }

    /** Cette methode ecrit une exception avec la pile dans la console
        @param ex l'exception a ecrire
    */
    public static void ecrireException(Throwable ex){
        ecrireString(ex.toString());
        ex.printStackTrace(System.out);
    }
}  


