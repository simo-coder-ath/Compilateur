parser grammar EMJParser;

options { tokenVocab = EMJLexer; }

// parser root, everything must start from here
root : carte EOF | programme EOF


 | instruction EOF;





carte : MAP_KEY WITH INT_VALUE COMMA INT_VALUE COMMA orientation SEMICOLON lignescarte ;



orientation : UP | DOWN | RIGHT | LEFT ;



lignescarte : lignecarte+ ;

lignecarte : casecarte+ ;






casecarte : POLICE | ROUTE  | VOLCAN | MAISON | CHANTIER | TRACTEUR | WATER | BARAGE ;


programme : IMPORT STRING_GUILL mainfonction fonction* ;





mainfonction : VOID_TYPE MAIN_FONCTION LEFT_PARENTH RIGHT_PARENTHE LEFT_CUR instruction* return_instru RIGHT_CUR   ;






fonction : fonctype EMOJI_ID LEFT_PARENTH parametres? RIGHT_PARENTHE  LEFT_CUR instruction* return_instru RIGHT_CUR ;






parametres : parametre (COMMA parametre)* ;




parametre : type EMOJI_ID  ;



return_instru :
 RETURN expressiondroite SEMICOLON
 |RETURN VOID_TYPE SEMICOLON
 |RETURN SEMICOLON
 |VOID_TYPE SEMICOLON

 ;





type : INT_TYPE | CHAR_TYPE | STRING_TYPE | BOOL_TYPE   |   TUPLE_TYPE LEFT_PARENTH type RIGHT_PARENTHE  ;


fonctype : type | VOID_TYPE  ;



bloc : LEFT_CUR instruction* RIGHT_CUR  ;










instruction : declarationvariable | assign |  return_instru |  affectation | appelfonction SEMICOLON | instructionpredefinie | condition | boucle ;









assign : expressiongauche EQUAL expressiondroite SEMICOLON ;




declarationvariable : type EMOJI_ID (EQUAL expressiondroite) ? SEMICOLON ;








affectation : INT_TYPE EMOJI_ID EQUAL expressionentiere SEMICOLON

               | STRING_TYPE EMOJI_ID EQUAL STRING_GUILL SEMICOLON

                | CHAR_TYPE EMOJI_ID EQUAL CHAR_APP SEMICOLON
                |  BOOL_TYPE EMOJI_ID EQUAL expressionboolean SEMICOLON ;



expressiongauche : EMOJI_ID  |  EMOJI_ID INDEX_ONE  |   EMOJI_ID INDEX_TWO ;



expressiondroite :  expressionboolean | expressionentiere | expressiongauche |  CHAR_APP | STRING_GUILL   | tuple ;









tuple : LEFT_PARENTH expressiondroite COMMA expressiondroite RIGHT_PARENTHE ;





appelfonction : EMOJI_ID LEFT_PARENTH arguments? RIGHT_PARENTHE ;





arguments : argument (COMMA argument)* ;
argument : expressiondroite ;





expressionentiere : terme ( ( PLUS | MINUS ) terme)* ;


terme : facteur ((FOIS | SLASH) facteur )* ;






facteur : INT_VALUE |MINUS facteur |  expressiongauche | appelfonction | LEFT_PARENTH expressionentiere RIGHT_PARENTHE ;



expressionboolean :   relation ((AND | OR)relation)* ;


relation : expressionentiere comparateur expressionentiere  | TRUE | FALSE | NOT relation | LEFT_PARENTH expressionboolean RIGHT_PARENTHE ;





comparateur : PETIT | GRAND | P_OU_EGAL | G_OU_EGAL | EGAL | DIFF | EQUAL;








instructionpredefinie : STOP LEFT_PARENTH RIGHT_PARENTHE SEMICOLON
                        |  SON LEFT_PARENTH RIGHT_PARENTHE SEMICOLON
                         | LUM LEFT_PARENTH RIGHT_PARENTHE SEMICOLON
                         | UP LEFT_PARENTH expressionentiere RIGHT_PARENTHE SEMICOLON
                         | DOWN LEFT_PARENTH expressionentiere RIGHT_PARENTHE SEMICOLON
                         | RIGHT LEFT_PARENTH expressionentiere RIGHT_PARENTHE SEMICOLON
                         | LEFT LEFT_PARENTH expressionentiere RIGHT_PARENTHE SEMICOLON
                         | CRAZY LEFT_PARENTH expressionentiere RIGHT_PARENTHE SEMICOLON ;





condition :

IF LEFT_PARENTH expressionboolean RIGHT_PARENTHE bloc ELSE LEFT_CUR SKIP_EMO SEMICOLON RIGHT_CUR



|

IF LEFT_PARENTH expressionboolean RIGHT_PARENTHE bloc ELSE bloc



;



boucle : WHILE LEFT_PARENTH expressionboolean RIGHT_PARENTHE bloc
        | FOR LEFT_PARENTH expressionentiere RIGHT_PARENTHE bloc  ;


test : left EQUAL right SEMICOLON;

left : INT_TYPE EMOJI_ID;

right : INT_VALUE;