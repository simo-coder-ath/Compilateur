package be.unamur.info.b314.compiler.emj;

import java.util.stream.Collectors;

/*
  Fonctionnement general :
        - Recevoir une string contenant les emojis
 - Itérer sur chaque codepoint Unicode de la string
 - Pour chaque codepoint, construire un segment "XXXXX"
        Joindre les segments avec "_" et prefix par "var_"

 */



public class EMJnameconverter {





    public String convert (String emojiId){
        return "var_" + emojitohexstring(stripcoch(emojiId));
    }



public String convertFunction(String emojiId) {

        return "func_" + emojitohexstring(stripcoch(emojiId));

}











private String emojitohexstring(String lig){

        return lig.codePoints().mapToObj(this::tohex).collect(Collectors.joining("_"));
}


private String stripcoch(String lig){

        if (lig != null && lig.startsWith("[") && lig.endsWith("]")) {
            return lig.substring(1, lig.length() - 1);

        }

        return lig;

}









private String tohex (int codepoint ){

        return Integer.toHexString(codepoint).toUpperCase();

}

}