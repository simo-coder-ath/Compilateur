package be.unamur.info.b314.compiler.emj;

import be.unamur.info.b314.compiler.EMJLexer;
import be.unamur.info.b314.compiler.EMJParser;
import be.unamur.info.b314.compiler.EMJParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/*
visite l'ANTLR comme EMJVisitor mais au lieu
  de vérifier la semantique elle produit du code micropython valide

 */


public class EmjCode extends EMJParserBaseVisitor<String> {


    private boolean besoinrandom = false;


private final StringBuilder output = new StringBuilder();
private int indentlevel = 0;
private int loopprof = 0 ;
private final EMJnameconverter nameconvert = new EMJnameconverter();

private String initialorientation = "north";






private String buildcutebothelper(){









    StringBuilder ssb = new StringBuilder();



    ssb.append("from cutebot import *\n");
    ssb.append("import music\n");


    if (besoinrandom) {
        ssb.append("import random\n");


    }ssb.append("\n");
    ssb.append("current_orientation = '" + initialorientation + "'\n");
    ssb.append("\n");







return ssb.toString();

}






public String generate(EMJParser.RootContext tree){

    visit(tree);
    return output.toString();

}




private String indent(){

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < indentlevel ; i++ ){
        sb.append("    ");
    }

    return sb.toString();




}














private void emit(String ligne ){

    output.append(indent()).append(ligne).append("\n");

}

private void emitblank(){

    output.append("\n");

}


    @Override
public String visitRoot(EMJParser.RootContext ctx){



    if (ctx.carte() != null ){
        visit(ctx.carte());
    }else if (ctx.programme() != null){
        visit(ctx.programme());

    }




    return null ;



}



    @Override
public String visitCarte(EMJParser.CarteContext ctx ){






    int width = Integer.parseInt(ctx.INT_VALUE(0).getText());


    int height = Integer.parseInt(ctx.INT_VALUE(1).getText());




    String orientation = orientationtostring(ctx.orientation());



    emit("map_width = " + width);
    emit("map_height = " + height);
    emit("map_orientation = '" + orientation + "'");
    emitblank();




    emit("grid = [");



    indentlevel++ ;





    for (EMJParser.LignecarteContext ligne : ctx.lignescarte().lignecarte()) {


        StringBuilder rw = new StringBuilder(indent() + "[");
        List<EMJParser.CasecarteContext> cases = ligne.casecarte();


        for (int i= 0 ; i< cases.size(); i++ ){


            rw.append ("'").append(casetostring(cases.get(i))).append("'");

            if(i< cases.size() -1 ) rw.append(", ");
        }




        rw.append("],");
        emit(rw.toString());

    }
    indentlevel --;
    emit("]");


    return null;









}









private String orientationtostring(EMJParser.OrientationContext ctx){

    if (ctx.UP()    != null)    return "north";
    if (ctx.DOWN()  != null)     return "south";
    if (ctx.RIGHT() != null)    return "east";
    if (ctx.LEFT()  != null)     return "west";



    return "north";


}














private String casetostring(EMJParser.CasecarteContext ctx ){




    if (ctx.POLICE()   != null) return "police";


    if (ctx.BARAGE()   != null) return "thief";

    if (ctx.ROUTE()    != null) return "road";


    if (ctx.VOLCAN()   != null) return "volcano";

    if (ctx.MAISON()   != null) return "house";

    if (ctx.CHANTIER() != null) return "work";


    if (ctx.TRACTEUR() != null) return "tractor";


    if (ctx.WATER()    != null) return "water";



return "unkown";











}






























@Override
public String visitProgramme(EMJParser.ProgrammeContext ctx){

output.append(buildcutebothelper());



    for (EMJParser.FonctionContext f : ctx.fonction()){

        visit(f);
        emitblank();
    }







    visit(ctx.mainfonction());




    emitblank();


    emit("main()");








    return null;
}










@Override
public String visitMainfonction(EMJParser.MainfonctionContext ctx ){



    emit("def main():");

    indentlevel++;



    if(ctx.instruction().isEmpty() && ctx.return_instru()== null){




        emit("pass");


    }

    else {



        for (EMJParser.InstructionContext instr : ctx.instruction()){


            visit(instr);

        }


        if(ctx.return_instru() != null ){
            visit(ctx.return_instru());



        }
    }




    indentlevel --;
    return null;





}











@Override
public String visitFonction(EMJParser.FonctionContext ctx ){

    String funcname = nameconvert.convertFunction(ctx.EMOJI_ID().getText());




    StringBuilder params = new StringBuilder();



    if (ctx.parametres() != null) {

    List<EMJParser.ParametreContext> paramlist = ctx.parametres().parametre();



    for  (int i = 0 ;i < paramlist.size(); i++){

        params.append(nameconvert.convert(

                paramlist.get(i).EMOJI_ID().getText()
        ));
        if (i < paramlist.size() - 1 )params.append(", ");

    }
    }




    emit("def" + funcname + "(" + params + "):" );



indentlevel++;



if (ctx.instruction().isEmpty() && ctx.return_instru() == null){


    emit("pass");


}else {


    for (EMJParser.InstructionContext instr : ctx.instruction()){



        visit(instr);

    }



    if(ctx.return_instru() != null ){


        visit(ctx.return_instru());

    }



}







indentlevel --;



return null;


}













    @Override
    public String visitReturn_instru(EMJParser.Return_instruContext ctx) {


        if (ctx.expressiondroite() != null) {


            emit("return " + visit(ctx.expressiondroite()));



        }

        else {



            emit("return");



        }



        return null;



    }






























        @Override
    public String visitInstruction(EMJParser.InstructionContext ctx) {



        if (ctx.appelfonction() != null) {


            emit(visit(ctx.appelfonction()));


            return null;




        }




        if (ctx.return_instru() != null) {



            visit(ctx.return_instru());



            return null;
        }








        return visitChildren(ctx);
    }








    @Override
    public String visitDeclarationvariable(EMJParser.DeclarationvariableContext ctx) {



        String varname = nameconvert.convert(ctx.EMOJI_ID().getText());


        if (ctx.expressiondroite() != null) {


            emit(varname + " = " + visit(ctx.expressiondroite()));


        } else {


            emit(varname + " = None");


        }



        return null;
    }






    @Override
    public String visitAffectation(EMJParser.AffectationContext ctx) {

        String varname = nameconvert.convert(ctx.EMOJI_ID().getText());

        String value;

        if (ctx.expressionentiere() != null) {

            value = visit(ctx.expressionentiere());

        }
        else if (ctx.expressionboolean() != null) {


            value = visit(ctx.expressionboolean());



        }
        else if (ctx.STRING_GUILL() != null) {

            value = ctx.STRING_GUILL().getText();


        }
        else if (ctx.CHAR_APP() != null) {


            value = ctx.CHAR_APP().getText();



        } else {


            value = "None";

        }



        emit(varname + " = " + value);



        return null;



    }




    @Override
    public String visitAssign(EMJParser.AssignContext ctx) {


        String left  = visit(ctx.expressiongauche());


        String right = visit(ctx.expressiondroite());


        emit(left + " = " + right);



        return null;



    }




















         @Override
    public String visitExpressiongauche(EMJParser.ExpressiongaucheContext ctx) {

        String varname = nameconvert.convert(ctx.EMOJI_ID().getText());



        if (ctx.INDEX_ONE() != null) return varname + "[0]";


        if (ctx.INDEX_TWO() != null) return varname + "[1]";




        return varname;



    }






































    @Override
    public String visitExpressiondroite(EMJParser.ExpressiondroiteContext ctx) {










        if (ctx.expressionboolean() != null)    return visit(ctx.expressionboolean());


        if (ctx.expressionentiere() != null)        return visit(ctx.expressionentiere());


        if (ctx.expressiongauche()  != null)     return visit(ctx.expressiongauche());

        if (ctx.CHAR_APP()          != null)    return ctx.CHAR_APP().getText();




        if (ctx.STRING_GUILL()      != null)    return ctx.STRING_GUILL().getText();



        if (ctx.tuple()             != null)        return visit(ctx.tuple());




        return "";


    }







            @Override
    public String visitTuple(EMJParser.TupleContext ctx) {


        String v1 = visit(ctx.expressiondroite(0));


        String v2 = visit(ctx.expressiondroite(1));


        return "[" + v1 + ", " + v2 + "]";



    }










            @Override
    public String visitExpressionentiere(EMJParser.ExpressionentiereContext ctx) {

        StringBuilder sb = new StringBuilder();


        List<EMJParser.TermeContext> termes = ctx.terme();


        sb.append(visit(termes.get(0)));



        int opindex = 1;

        for (int i = 1; i < termes.size(); i++) {


            ParseTree opnode = ctx.getChild(opindex);


            sb.append(" ").append(opnode.getText()).append(" ")


                    .append(visit(termes.get(i)));



            opindex += 2;



        }







        return sb.toString();
    }














        @Override
    public String visitTerme(EMJParser.TermeContext ctx) {



        StringBuilder sb = new StringBuilder();


        List<EMJParser.FacteurContext> facteurs = ctx.facteur();


        sb.append(visit(facteurs.get(0)));







        int opindex = 1;

        for (int i = 1; i < facteurs.size(); i++) {

            ParseTree opnode = ctx.getChild(opindex);

            sb.append(" ").append(opnode.getText()).append(" ")

                    .append(visit(facteurs.get(i)));



            opindex += 2;



        }





        return sb.toString();


    }











            @Override
    public String visitFacteur(EMJParser.FacteurContext ctx) {




        if (ctx.INT_VALUE()         != null)

            return ctx.INT_VALUE().getText();




        if (ctx.MINUS()             != null)

            return "-" + visit(ctx.facteur());


        if (ctx.expressiongauche()  != null)

            return visit(ctx.expressiongauche());



        if (ctx.appelfonction()     != null)

            return visit(ctx.appelfonction());



        if (ctx.expressionentiere() != null) {


            return "(" + visit(ctx.expressionentiere()) + ")";



        }








        return "";
    }












    @Override
    public String visitExpressionboolean(EMJParser.ExpressionbooleanContext ctx) {



        StringBuilder sb = new StringBuilder();



        List<EMJParser.RelationContext> relations = ctx.relation();



        sb.append(visit(relations.get(0)));





        int opindex = 1;


        for (int i = 1; i < relations.size(); i++) {



            TerminalNode opnode = (TerminalNode) ctx.getChild(opindex);




            int tokenType = opnode.getSymbol().getType();


            String pyop = (tokenType == EMJLexer.AND) ? "and" : "or";



            sb.append(" ").append(pyop).append(" ")


                    .append(visit(relations.get(i)));



            opindex += 2;



        }







        return sb.toString();



    }















        @Override
    public String visitRelation(EMJParser.RelationContext ctx) {


        if (ctx.TRUE()  != null) return "True";



        if (ctx.FALSE() != null) return "False";






        if (ctx.NOT() != null) {





            return "not " + visit(ctx.relation());

        }





        if (ctx.expressionboolean() != null) {




            return "(" + visit(ctx.expressionboolean()) +


                    ")";






        }







        if (ctx.expressionentiere().size() == 2) {



            String left  = visit(ctx.expressionentiere(0));


            String op    = ctx.comparateur().getText();



            String right = visit(ctx.expressionentiere(1));




            return left + " " + op + " " + right;







        }








        if (ctx.expressionentiere().size() == 1) {



            return visit(ctx.expressionentiere(0));




        }

        return "";




    }












            @Override
    public String visitAppelfonction(EMJParser.AppelfonctionContext ctx) {







        String funcname = nameconvert.convertFunction(ctx.EMOJI_ID().getText());


        StringBuilder args = new StringBuilder();



        if (ctx.arguments() != null) {


            List<EMJParser.ArgumentContext> argList = ctx.arguments().argument();

            for (int i = 0; i < argList.size(); i++) {


                args.append(visit(argList.get(i).expressiondroite()));


                if (i < argList.size() - 1) args.append(", ");



            }


        }








        return funcname + "(" + args + ")";



    }










































        @Override
    public String visitInstructionpredefinie(EMJParser.InstructionpredefinieContext ctx) {





      if (ctx.STOP() != null ){

          emit("cutebot.stop()");





          return null ;
      }









      if (ctx.SON()  != null ) {




          emit("music.play(music.built_in_melody(Melody.POWER_UP), music.PlaybackMode.UNTIL_DONE)");

          return null;
      }






            if (ctx.LUM()  != null ) {




                emit("cutebot.set_car_light(cutebot.CarLights.LEFT, 0xff0000)");


                emit("cutebot.set_car_light(cutebot.CarLights.RIGHT, 0xff0000)");



                return null;
            }








            if (ctx.UP()  != null ) {


String step = visit(ctx.expressionentiere());


                emit("cutebot.forward(50)");


                emit("basic.pause(" + step + " * 500)");


               emit("cutebot.stop()");





                return null;
            }











            if (ctx.DOWN()  != null ) {


                String step = visit(ctx.expressionentiere());


                emit("cutebot.backward(50)");


                emit("basic.pause(" + step + " * 500)");


                emit("cutebot.stop()");





                return null;
            }
























            if (ctx.RIGHT()  != null ) {


                String step = visit(ctx.expressionentiere());


                emit("cutebot.turn_right(50)");





                emit("basic.pause(400)");


                emit("cutebot.forward(50)");


                emit("basic.pause(400)");








                emit("basic.pause(" + step + " * 500)");


                emit("cutebot.stop()");





                return null;
            }
















            if (ctx.LEFT()  != null ) {


                String step = visit(ctx.expressionentiere());


                emit("cutebot.turn_left(50)");





                emit("basic.pause(400)");



                emit("cutebot.forward(50)");



                emit("basic.pause(" + step + " * 500)");


                emit("cutebot.stop()");





                return null;
            }

















            if (ctx.CRAZY()  != null ) {

                besoinrandom = true;


                String dureee = visit(ctx.expressionentiere());


                emit("import random");


                emit("for _crazy in range(\" + dureee + \"):");
indentlevel ++;


                emit("_dir = random.randint(0, 3)");



                emit("if _dir == 0: cutebot.forward(80)");



                emit("elif _dir == 1: cutebot.backward(80)");

                emit("elif _dir == 2: cutebot.turn_left(80)");

                emit("else:  cutebot.turn_right(80)");
                emit("basic.pause(1000)");

       indentlevel --;



                emit("cutebot.stop()");





                return null;
            }







            return null;
    }









        @Override
    public String visitCondition(EMJParser.ConditionContext ctx) {



        String cond = visit(ctx.expressionboolean());


        emit("if " + cond + ":");



        indentlevel++;



        visitBloc(ctx.bloc(0));


        indentlevel--;



        emit("else:");



        indentlevel++;



        if (ctx.bloc().size() > 1) {



            visitBloc(ctx.bloc(1));



        }


        else {





            emit("pass");



        }



        indentlevel--;

        return null;







    }











        @Override
    public String visitBoucle(EMJParser.BoucleContext ctx) {



        if (ctx.WHILE() != null) {



            String cond = visit(ctx.expressionboolean());


            emit("while " + cond + ":");







            indentlevel++;
            visitBloc(ctx.bloc());
            indentlevel--;

        } else if (ctx.FOR() != null) {


            String count   = visit(ctx.expressionentiere());


            String loopVar = "i_" + loopprof;


            loopprof++;



            emit("for " + loopVar + " in range(" + count + "):");




            indentlevel++;


            visitBloc(ctx.bloc());


            indentlevel--;

            loopprof--;




        }







        return null;



    }











        @Override
    public String visitBloc(EMJParser.BlocContext ctx) {



        if (ctx.instruction().isEmpty()) {



            emit("pass");



            return null;
        }









        for (EMJParser.InstructionContext instr : ctx.instruction()) {


            visit(instr);
        }


        return null;



    }



}