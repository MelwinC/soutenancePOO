package fr.cda.util;

import java.io.*;
import java.util.*;

/** La classe Terminal permet de réaliser ses premiers programmes Java en permettant d'afficher dans la console d'exécution des données de type différents, et en permettant de saisir au clavier des données de type différents.<BR>
    Elle permet aussi de lire et écrire un fichier texte
    Cette classe contient que des méthodes statiques. */
public class Terminal{    

    // Le buffer standard  de lecture = le clavier
    private static BufferedReader in =
        new BufferedReader(new InputStreamReader(System.in));

    /** Cette méthode lit un fichier texte et retourne le contenu du fichier sous la forme d'un  tableau de String dont chaque element est une ligne du fichier.
        @param nomFichier le nom du fichier qui doit être dans le répertoire courant.
        @return String[] le contenu du fichier. 
        @exception TerminalException (de type RuntimeException) si erreur d'écriture<BR>
        Rappel : Une exception de type RuntimeException n'a pas l'obligation d'être capturée.
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

            // On enleve le caractère '\r' code 13 qui est ajouté en Windows
            // Les fins de ligne dans un fichier texte créé sous Windows
            //  se termine par \r\n.
            // Il faut enlever le \r car il a des effets perturbant sur
            //  la méthode System.out.print et est pris comme un caractère de plus
            //  qu'il faut éliminer
            //  
            String texte = str.replaceAll(""+(char)(13),"");
            
            // Les lignes du fichier sont mises dans un tableau
            //
            String[] mots = texte.split("\n");

            return(mots);
        }
        catch(Exception ex) {return null;}
    }

    /** Cette méthode permet de créer un fichier texte à partir du contenu d'un StringBuffer.
        @param nomFichier Le nom du fichier qui est créé dans le répertoire courant
        @param strbuf Le StringBuffer contenant le texte à écrire. 
        @exception TerminalException (de type RuntimeException) si erreur d'écriture
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

    /** Cette méthode lit une chaîne de caractère
        @return String la chaîne saisie dans la console d'exécution
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

    /** Cette méthode lit un entier
        @return int L'entier saisi dans la console d'exécution
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

    /** Cette méthode lit un boolean (false ou true)
        @return boolean Le boolean saisi dans la console d'exécution
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

    /** Cette méthode lit un double
        @return double Le double saisi dans la console d'exécution
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

    /** Cette méthode lit un caractère.
       @exception TerminalException (de type RuntimeException) si erreur de lecture.<BR>
       Si on saisit plus d'1 caractère alors le caractère retourné est le premier.
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

    /** Cette méthode écrit une chaine et ne revient pas à la ligne.
        @param s la chaine &agrave; &eacute;crire
    */
    public static void ecrireString(String s){ // Afficher un String
        System.out.print(s);
    }

    /** Cette méthode écrit une chaine et revient à la ligne.
        @param s la chaine &agrave; &eacute;crire
    */
    public static void ecrireStringln(String s) // Afficher un String
    {
        ecrireString(s);
        sautDeLigne();
    }

    /** Cette méthode écrit un entier et ne revient pas à la ligne.
        @param i l'entier à écrire
    */
    public static void ecrireInt(int i)  // Afficher un entier
    {
        ecrireString(""+i);
    }

    /** Cette méthode écrit un entier et revient à la ligne.
        @param i l'entier à écrire
    */
    public static void ecrireIntln(int i)  // Afficher un entier
    {
        ecrireString(""+i);
        sautDeLigne();
    }

    /** Cette méthode écrit un booléan et ne revient pas à la ligne.
        @param b le booléen à écrire
    */
    public static void ecrireBoolean(boolean b){
        ecrireString(""+b);
    }

    /** Cette méthode écrit un booléan et revient à la line.
        @param b le booléen à écrire
    */
    public static void ecrireBooleanln(boolean b){
        ecrireString(""+b);
        sautDeLigne();
    }

    /** Cette méthode écrit un double et ne revient pas à la ligne.
        @param d le double à écrire
    */
    public  static void ecrireDouble(double d)  // Afficher un double
    {
        ecrireString(""+d);
    }

    /** Cette méthode écrit un double et revient à la ligne.
        @param d le double à écrire
    */
    public  static void ecrireDoubleln(double d)  // Afficher un double
    {
        ecrireDouble(d);
        sautDeLigne();
    }

    /** Cette méthode écrit un caractère et ne revient pas à la ligne.
        @param c le caractère à écrire
    */
    public  static void ecrireChar(char c)  // Afficher un caractere
    {
        ecrireString(""+c);
    }  

    /** Cette méthode écrit un caractère et revient à la ligne.
        @param c le caractère à écrire
    */
    public  static void ecrireCharln(char c)  // Afficher un caractere
    {
        ecrireChar(c);
        sautDeLigne();
    }

    /** Cette méthode revient à la ligne.
    */
    public static void sautDeLigne(){
        try{
            System.out.println();
        }catch(Exception ex){
            exceptionHandler(ex);
        }
    }

    /** Cette méthode retourne l'exception TerminalException
    */
    protected static void exceptionHandler(Exception ex){
        TerminalException err = new TerminalException(ex);
        throw err;
    }

    /** Cette méthode écrit une exception avec la pile dans la console
        @param ex l'exception à écrire
    */
    public static void ecrireException(Throwable ex){
        ecrireString(ex.toString());
        ex.printStackTrace(System.out);
    }
}  


